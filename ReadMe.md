CarParkAPIAService - for consumers to know that network calls are made  when calling this service

Adding access token to memory and only requesting it when expiry is close to server time. This can reduce 1 network call.

 Docker artifacts are instructions, they are pushed to git. So credentials into env so the are not hardcoded into docker artifacts.
 
 
 
 scheduled a task. ideally, it should be a clustered scheduler so that the scheduler can run seamlessly on any server.
 
 rest template - token from interceptor
 token only if it is expired to avid network calls
 
static information. 

Optimizations Done

1. Version column that hibernate can maintain for optimistic locking to mitigate concurrency issues. 

2. The token is called via the rest template interceptor thereby abstracting this grunt work. We have also checked if the token is valid in order to reduce network calls. 


Optimizations foreseen but could not implement

Spatial index and Point data type for performant queries on distance field. 


Scalability   
 
 1. When carpark availability task is called by scheduler, the live data collected is first checked with map  - CarParkCacheMap - so that only those records are updated which have changed. 
 
    tradeoff - this increases complexity of maintaining a in memory cache and makes it stateful. If application server is clustered, this memory cache will have to be offloaded onto a distributed cache. 
 
 
 
 2. There are presently ~ 2000 car parks. So as a maintenance task, we are finding lat long of all car parks by calling the api/common/convert/3414to4326 API and loading them into the database so that any user request does not have to make outbound API calls.
 
    tradeoff - We accrue a management API to load carpark latlongs. If this was being done on a larger scale say, 20,000 car parks, it would not be wise to make 20,000 API calls at once. 
    Concerns will range from being rate limited to account being suspended for a day. It is important to know how many requests does the vendor (e.g in this case, OpenMap) allow a user before the
    vendor deems any "feature" as supposed DOS attack.     
   