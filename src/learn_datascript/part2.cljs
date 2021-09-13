(ns learn-datascript.part2
  (:require [datascript.core :as d]))

(comment
  ;; https://udayv.com/blog/2016-05-06-datascript101-chapter2-uniqueness-and-indexing/
  
  ;; Identities will help in identifying the entities instead of relying on
  ;; Datascript's IDs

  ;; Hey this :car/model field is going to be unique and 
  ;; will be used to identify this entity (a car in this case).

  ;; For makers the Unique Identity will be email
  (def schema {:maker/email {:db/unique :db.unique/identity}

               :car/model {:db/unique :db.unique/identity}
               :car/maker {:db/type :db.type/ref}
               :car/colors {:db/cardinality :db.cardinality/many}})
  
  ;; Create DB with schema
  (def conn (d/create-conn schema))

  ;; Lets insert a new maker and car along with it
  (d/transact! conn [{:maker/email "ceo@bmw.com"
                      :maker/name "BMW"}
                     {:car/model "E39530i"
                      :car/maker [:maker/email "ceo@bmw.com"]
                      :car/name "2003 530i"}])
  ;; :db.type/ref can point to an :db.unique/identity
  ;; Like we did here getting [:maker/email "ceo@bmw.com"]
  ;; effectively making `:car/maker` pointing to "BMW"
  ;; This is called a LOOKUP REF

  ;; Querying

  ;; Lets see what the maker looks like
  (d/q '[:find ?e ?name ?email
         :where [?e :maker/name ?name]
                [?e :maker/email ?email]] @conn)
  
  ;; Concise entity lookups
  ;; Can be done only with identities

  ;; This will throw error
  (d/entity @conn [:car/name "2003 530i"])

  ;; This will work (since `:car/model` is unique identity)
  (d/entity @conn [:car/model "E39530i"])

  ;; Once we have the entity we can get other stuff that belongs to it
  (-> 
   (d/entity @conn [:car/model "E39530i"])
   (get :car/name))
  
  ;; Insert another car using the lookup ref `[:maker/email "ceo@bmw.com"]`
  (d/transact! conn [{:car/model "E39520i"
                      :car/maker [:maker/email "ceo@bmw.com"]
                      :car/name "2003 520i"}])
  
  ;; We can also use lookup refs while querying
  ;; Lets find all the cars made by BMW
  (d/q '[:find [?name ...]
         :where [?c :car/maker [:maker/email "ceo@bmw.com"]]
                [?c :car/name ?name]]
        @conn)
  ;; This is an alternative to the query in part1
  ;; (d/q '[:find ?name
  ;;        :where
  ;;        [?e :maker/name "BMW"]
  ;;        [?c :car/maker ?e]
  ;;        [?c :car/name ?name]] @conn)

  ;; Since we are using lookup refs for a unique id in the queries we can update the maker fields
  ;; without updating anyof the queries
  ;; Lets update the BMW -> BMW Motors
  (d/transact! conn [{:maker/email "ceo@bmw.com"
                      :maker/name "BMW Motors"}])
  
  ;; The prev query works just fine
  (d/q '[:find [?name ...]
         :where [?c :car/maker [:maker/email "ceo@bmw.com"]]
                [?c :car/name ?name]]
       @conn)

    ;; Takeaways
    ;;  Lookup-refs are interchangeable with entity IDs almost everywhere, Datascript will complain when they’re not, so go ahead and use them liberally.
    ;;  Use identity and uniqueness to model your domain specific “identifiers”.
 ,)