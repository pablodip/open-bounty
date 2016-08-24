(ns commiteth.db.users
  (:require [commiteth.db.core :refer [*db*] :as db]
            [clojure.java.jdbc :as jdbc])
  (:import [java.util Date UUID]))

(defn create-user
  [login name email token]
  (jdbc/with-db-connection [con-db *db*]
    (db/create-user! con-db
      {:id      (str (UUID/randomUUID))
       :login   login
       :name    name
       :email   email
       :token   token
       :address nil
       :created (new Date)})))

(defn get-user
  [login]
  (jdbc/with-db-connection [con-db *db*]
    (db/get-user con-db {:login login})))

(defn exists?
  [login]
  (jdbc/with-db-connection [con-db *db*]
    (some? (db/get-user con-db {:login login}))))

(defn update-user-address
  [login address]
  (jdbc/with-db-connection [con-db *db*]
    (db/update-user-address! con-db {:login login :address address})))

(defn update-user-token
  "Updates user token and returns updated user"
  [login token]
  (jdbc/with-db-connection [con-db *db*]
    (db/update-user-token! con-db {:login login :token token})))
