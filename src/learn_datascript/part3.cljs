(ns learn-datascript.part3
  (:require [datascript.core :as d]))

(comment
  ;; https://udayv.com/blog/2016-06-15-datascript101-chapter3-fetching-data/

  ;; Lets create schema for users
  (def schema {:user/id {:db.unique :db.unique/identity}
               :user/name {}
               :user/age {}
               :user/parent {:db.valueType :db.type/ref
                             :db.cardinality :db.cardinality/many}})
  
  ;; Lets create the DB
  (def conn (d/create-conn schema))

  ;; Lets add some users now
  (d/transact! conn
               [{:user/id "1"
                 :user/name "alice"
                 :user/age 27}
                {:user/id "2"
                 :user/name "bob"
                 :user/age 29}
                {:user/id "3"
                 :user/name "kim"
                 :user/age 2
                 :user/parent [[:user/id "1"]
                               [:user/id "2"]]}
                {:user/id "4"
                 :user/name "aaron"
                 :user/age 61}
                {:user/id "5"
                 :user/name "john"
                 :user/age 39
                 :user/parent [[:user/id "4"]]}
                {:user/id "6"
                 :user/name "mark"
                 :user/age 34}
                {:user/id "7"
                 :user/name "kris"
                 :user/age 8
                 :user/parent [[:user/id "4"]
                               [:user/id "5"]]}])
  
  ;; Querying
  ;; Lets pull out all the IDs and names of users
  (d/q '[:find ?e ?name
         :where [?e :user/id]
                [?e :user/name ?name]]
        @conn)
  
  ;; Get all names in a single vector
  ;; Here we are saying lookup all the datoms with `:user/name` attr and give me the name
  ;; Since we don't need entity-id we are ignoring it using `_`
  ;; And finally fetching the results as a vector using `[?name ...]`
  (d/q '[:find [?name]
         :where [_ :user/name ?name]]
       @conn)

 ,)