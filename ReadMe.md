
 INSTRUCTIONS TO RUN PROJECT
 
 mysql runs on port 3307 and spring server runs on port 8080. These two ports have to be free to be assigned by docer 
 
1. clone the repo using below url. You will need git installed on your machine and cmd/bash access  (cmd for windows)

    git clone https://github.com/ricksonmenezes/wego-assignment.git

2. You will see a folder called wego-assignment. Enter that folder. On a test machine, from cmd, I was only able to see the read me file. 
    You can run command: 
        git branch 
    and it will display the name of the branch "main". 

3. Run the command: docker-compose up --build
    This command will download mvn that will build the jar file and copy it to app docker image and then run two containers from the two images
    of mysql and java. The java I have used is Java 8 as I am most familiar with it.
    
    If all goes well, there is no need to run any other command.
    
4. You could use postman, curl or even the browser to call API 
    
    http://localhost:8080/carparks/nearest?latitude=1.37326&longitude=103.897&page=1&per_page=10       


 ARTIFACTS:
 Docker artifacts are instructions, they are pushed to git. So credentials into env so the are not hardcoded into docker artifacts. 
 
 TEST APIs
 
 Every sub feature - dumping csv data, pulling live data and dumping the changes, converting SVy21 to latlong etc - all have test APIs
 
 
 
 Database artifacts 
  
 CarPark
 
 The CSV that contains static data about carpark such as address, parking type etc that rarely change are dumped nto CarPark table. 
 
 CarParkAvailability
 
 Live data that has total and available lots according to lot type - car motorcycle, heavy vehical - are dumped into CarParkAvailability. 
 
 CORE LOGIC
 
 Query that can list all car parks by minimum distance when comparing lat/long point provided by user to latlong of car parks. Only compare this for 
 those car parks that have available lots.
 
 The simplicity of above logic is that we are not pulling hundreds of records from DB  and then analysing it at the application level.
  This analysis is offloaded to the database which does it best via SQL declarative syntax that is easy to control. 
 
 

SCHEDULER TASKS

1. There is a task - RetryCarParkInfo job that updates everything from the file into the CarPark table. As the download is out of scope, it assumes
   that the file from the resource folder is latest. This task runs every 30 mins. Only in case the SVY21 of any record changes, we also update its
   corresponding lat/long which is maintained in the same table.
   
2. Another task - RetryCarParkLiveData job calls car park availability API every 30 seconds and updates all records into CarParkAvailability table. 
   Next time it runs, it will only update the changes between the table and live data from API.
   
3. It is to be noted that although the RetryCarParkInfo can update the CarPark static data on a timely fashion, the /nearest API will not work 
   until CarPark data is not present. Hence I have used the strategy to load CarPark data on application startup for initial load.   
 

Optimizations Done

1. Version column that hibernate can maintain for optimistic locking to mitigate concurrency issues. 
 

Optimizations foreseen but could not implement

1. Spatial index and Point data type for performant queries on distance field. Presently,  the /nearest API clocks 100-200 ms

2. Adding foreign key relation ship between CarPark and CarParkAvailability

3. Adding scripts to docker  

4. Adding token to interceptor so that token call is abstracted and only done when token is expired there by reducing network calls.

5. Presently all car park data every 30 mins is indiscriminately updated. Using Javers library, we can check if there are changes between
   the csv car park data and DB data and only then update it. This would save us 2000 updates each time as changes to static data is rare.
   
6. Could not get time to move all the hard-coded API urls and one map credentials to a separate file like yaml etc.      


Scalability   
 
 1. When carpark availability task is called by scheduler, the live data pulled is first checked with map  - carparkDataMap - so that only those records are updated which have changed. 
 
    carparkDataMap is a cache that consists of CarPark total and available slots for each lotType of a CarPark Number
        Why are we maintainint it? If the task to update live data runs every 30 seconds , we will have around 2000-4000 unnecessary updates every half minute 
       If there were 20,000 car parks, that would mean almost 20-50K updates (some carpark nos have 1-3 slots). Hence we maintain a cache to only update car park that have lots changed 
      Cases: If cache doesn't contain CarParkNo key, it will be saved
             If carParkNo cache exists in cache but the lot type doesn't exist, slotTpe along with total and available lots will be added to CarParkAvailability  table and cache key updated
              If CarParNo cache exists in cache and the lot type exists and the total or available lots for that lot type has changed, it will be updated to CPA table and cache key updated
              If CarParNo cache exists in cache and the lot type exists and the total or available lots for that lot type has not changed, it will skip this live data */

    tradeoff - this increases complexity of maintaining a in memory cache and makes it stateful. If application server is clustered, this memory cache will have to be offloaded onto a distributed cache. 
 
    benefits - Every minute, we have only around 700-900 car parks available_lots that change. This is around 30% of the data being updated instead.  
 
 2. There are presently ~ 2000 car parks. So as a maintenance task, we are finding lat long of all car parks by calling the api/common/convert/3414to4326 API and loading them into the database so that any user request does not have to make outbound API calls.
 
    tradeoff - We accrue a management API to load carpark latlongs. If this was being done on a larger scale say, 20,000 car parks, it would not be wise to make 20,000 API calls at once. 
    Concerns will range from being rate limited to account being suspended for a day. It is important to know how many requests does the vendor (e.g in this case, OpenMap) allow a user before the
    vendor deems any "feature" as supposed DOS attack.     
  
 3. Conversion library instead of converter API: Initially integrated pt. 2 - converter API. But it is not scalable as it only allows 250 API calls a minute and that would mean that the system is busy for an initial 10 minutes. 
    Hence, integrated SVY21 https://github.com/cgcai/SVY21 library that does the conversion offline. So now, no more management API. 
    Tradeoff - conversion by OneMap  is authorotative. On the other hand, one would have to validate the conversion being done by any offline library. 
    
 FINDING NEAREST CAR PARK   
    
 1. Using MySQL ST_DISTANCE_SPHERE point function to calculate shortest distance. The order by is on ST_Distance_Sphere(point(user latlong),point(carparks_latlong)). I used Spring-JDBC template
 to return this result as it allowed me to write a clean native join  query + do pagination + do sorting. The issue with Spring JPA would be that having to do the trinity - 
  native join query + pagination + sorting on a sql function would need an ingenious customization of which the tradeoff is time. I also believe that one size does not fit all
  and I had to go with the tool that fit the purpose at the moment i.e I had a good handle on how ST_DIstance_Sphere works and went ahead with this approach. 
  
  The other approach could have been to sort on a geo-spatial column that is part of the entity. This would then allow a clean sort on an entity column. This kind of a 
  Point column is available in specialized hibernate libraries. I did not explore it too much due to time constraints.
  
  IMPROVIZATIONS
  
 1. I have used auto-update for table creation for the sake convenience. Obviously, no one does this on prod but it really speeds up work on initial proof of concept for which
    the assesment is a good candidate.
    
  
  KNOWN ISSUES
  
 1. when I log into the mysql service, I can see my sql artifacts - CarPar & CarParkAvailability. But I can also see two additional tables 
    with no data car_park and car_park_availability. perhaps this is the consequence of using the auto-update to create schemas. 
 
 2. Testing this once on an independent machine made mysql service start after Spring server initalization and hibernate could not get 
    communication inspite of the "depends on" docker instruction. Turns out that "depends on" only makes the spring container wait(depends)
     on the mysql container to run not to be healthy. I have added certain mysql health checks and made spring server depend on the health
     check. Hopefully this is not deprecated. In case app server on start-up has a communication link failure. One can run 
     docker-compose up --build again and it should work. 
  
  3. As one time load is done on start-up, restarting the app is again going to try to sync the car par static data.
     Due to time constraints have not been able to optimize this 
  
  4. As task is calling car par availability API every 30 seconds and I was not able to find documentation for /carpark-availability API on onemap, 
     I ignored the timestamp & update_timestamp variables. I have now realized that perhaps they indicate the last time since the carpark info
     changed. checking the last time the timestamp changed would have been a "go by the documentation" approach. What I have done is to directly 
     check if the available lots have changed without relying on the timestamp they provide of "last time since changed" i.e only if my assumptions 
     are true that that is indeed what that mean. In fact, if it is indeed true that that is what the timestamp means, I admit that I ought to have 
     dumped the entire data - I have only added carpakno, totalLots, availableLots as part of the CarParkAvailability model. This would mean to have 
     to futher normalize the table. items.timestamp could be part of a class member checking if there are any updates since the last time task queried
      live data.
      
      Alternatively, Not sure about the car park data but car par availability data is a good 
      candidate for mongo db collection.      