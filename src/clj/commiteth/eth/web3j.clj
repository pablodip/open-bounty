(ns commiteth.eth.web3j
    (:require [commiteth.eth.core :as eth]
            [commiteth.config :refer [env]])
  (:import [org.web3j
            protocol.Web3j
            protocol.http.HttpService
            crypto.Credentials
            crypto.WalletUtils]
           org.apache.http.message.BasicHeader))

(defn wallet-file-path []
  (env :eth-wallet-file))

(defn wallet-password []
  (env :eth-password))

(defn creds []
  (let [password  (wallet-password)
        file-path (wallet-file-path)]
    (if (and password file-path)
      (WalletUtils/loadCredentials
       password
       file-path)
      (throw (ex-info "Make sure you provided proper credentials in appropriate resources/config.edn"
                      {:password password :file-path file-path})))))

(defn create-web3j []
  (let [http-service (proxy
                       [HttpService]
                       [(eth/eth-rpc-url)]
                       (addHeaders [headers]
                         (when (env :eth-rpc-auth-enabled)
                           (.add headers (BasicHeader. "Authorization" (eth/authorization-header-val))))))]
    (Web3j/build http-service)))
