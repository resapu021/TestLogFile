package com.creditsuisse.groupid.creditsuisseartifactid;

public class CreateEntryObject {

private String id;  
private String state;
private String type;
private String host;
private String timestamp; 
  
public String getId() {  
return id;  
}  
  
public void setId(String id) {  
this.id = id;  
}  
  
public String getState() {  
return state;  
}  
  
public void setState(String state) {  
this.state = state;  
}
public String getType() {  
return type;  
}  
  
public void setType(String type) {  
this.type = type;  
}
  
public String getHost() {  
return host;  
}  
  
public void setHost(String host) {  
this.host=host;  
}
public String getTimeStamp() {  
return timestamp;  
}  
  
public void setTimeStamp(String timeStamp) {  
this.timestamp=timeStamp;  
} 
  
@Override  
public String toString() {  
// TODO Auto-generated method stub  
return "\"CreateEntryObject [id "+ id + ", state = " + state + ", type = " + type + "+ \", host = \" + host + \"+ \", timestamp = \" + timestamp + \"]";  
}  
  
}  
