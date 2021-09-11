# creditSuisse
This is an sample project to demonstrate how to handle event details from a file named logfile.txt using Java

# Important
Make sure you have JAVA 8 or above installed to run this test


Libraries support
Test Runner support
gson
hsqldb
json-simple
maven-surefire-plugin

More Information
1. Run TestEntry.java as RunAs->Java Application

Following Output is shown in Console:
Sep 11, 2021 9:23:19 PM org.hsqldb.persist.Logger logInfoEvent
INFO: checkpointClose start
Sep 11, 2021 9:23:19 PM org.hsqldb.persist.Logger logInfoEvent
INFO: checkpointClose synched
Sep 11, 2021 9:23:19 PM org.hsqldb.persist.Logger logInfoEvent
INFO: checkpointClose script done
Sep 11, 2021 9:23:19 PM org.hsqldb.persist.Logger logInfoEvent
INFO: checkpointClose end
Table Created Successfully
{"id":"scsmbstgra","state":"STARTED", "type":"APPLICATION_ LOG", "host":"12345", "timestamp" :1491377495212}
{"id" :"scsmbstgrb",  "state":"STARTED", "timestamp": 1491377495213}
{"id" :"scsmbstgrc", "state":"FINISHED" , "timestamp" :1491377495218}
{"id" :"scsmbstgra", "state":"FINISHED", "type":"APPLICATION_LOG", "host":"12345", "timestamp":1491377495217}
{"id" :"scsmbstgrc", "state":"STARTED", "timestamp": 1491377495210}
{"id" :"scsmbstgrb", "state":"FINISHED", "timestamp" :1491377495216}
1 rows effected
Rows inserted successfully
1 rows effected
Rows inserted successfully
1 rows effected
Rows inserted successfully
scsmbstgra | 5 | APPLICATION_ LOG | 12345 | true
scsmbstgrb | 3 | null | null | false
scsmbstgrc | 8 | null | null | true
Sep 11, 2021 9:23:20 PM org.hsqldb.persist.Logger logInfoEvent
INFO: Database closed


Implementation Details:
1. TestEntry.java starts the server dynamically, which internally creates eventDetails_tbl table
2. Takes the path to logfile.txt as an input argument
3. Creates and Entry Object for each json line by line using Gson (Gson ensures to handle large files (gigabytes))
4. HashMap hm stores data in Integer,CreateEntryObject
5. HashMap hm1 stores data in String,Object
6. Each iteration of hm1, inserts the data to DB
7. Query the DB
8. Deletes the table
9. Stops the Server
