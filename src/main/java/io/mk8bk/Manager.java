package io.mk8bk;

public class Manager {
   static private long IdCounter = 0;
   private final String username;
   public Manager(String username){
       this.username = username;
   }
}
