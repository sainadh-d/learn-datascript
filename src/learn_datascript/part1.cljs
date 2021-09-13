(ns learn-datascript.part1
  (:require [datascript.core :as d]))

(comment
  ;; https://udayv.com/blog/2016-04-28-datascript101-chapter1-initializing-inserting-and-querying-records/
  
  ;; Create schema -> Its not necessary to declare all the types of entities that we might
  ;; use in our project
  ;; Here we are saying, `:car/maker` will be a reference and `car/colors` will be an array
  (def schema {:car/maker {:db/type :db.type/ref}
               :car/colors {:db/cardinality :db.cardinality/many}})

  ;; Initialize DB with schema
  (def conn (d/create-conn schema))

  ;; transact! is used for updates to DB (insert/update/delete)
  ;; Here we are insert a "Honda" Car maker
  (d/transact! conn [{:maker/name "Honda"
                      :maker/country "Japan"}])
  ;; As you can see we did not create schema for maker, for still we were able to insert
  
  ;; Now we insert a maker and car. 
  ;; -1 is just a place holder. Datascript will use a unique ID
  ;; It is used here to avoid making multiple transact! calls, first to insert maker and then get the ID
  ;; and then insert the car
  (d/transact! conn [{:db/id -1
                      :maker/name "BMW"
                      :maker/country "Germany"}
                     {:car/maker -1
                      :car/name "i525"
                      :car/colors ["red" "green" "blue"]}])
  
  ;; Now lets find all the car-names whose maker is BMW
  (d/q '[:find ?name
         :where 
         [?e :maker/name "BMW"]
         [?c :car/maker ?e]
         [?c :car/name ?name]] @conn)
  
  ;; Now move on to part-2
 ,)
   