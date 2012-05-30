(ns carioca.ui
  (:use seesaw.core)
  (:import (javax.swing JFrame JLabel JButton)
           (java.io File)))

(native!)

(defn card-to-file
  [card]
  (letfn [(n [i] (if (number? i) i (name i)))]
     (str (n (get card 0)) "_" (n (get card 1)))))

(defn card-icon
  [card]
  (icon (File. (str "resources" File/separator (card-to-file card) ".png"))))

(defn card-label
  [card]
  (label :icon (card-icon card)))

(defn card-button
  [card]
  (button :icon (card-icon card)))

(defn player-hand-items
  [player-hand hide-first-card]
  (let [card-labels (vec (map card-label player-hand))]
    (interleave (if hide-first-card
                  (cons (card-label [:back :red]) (rest card-labels))
                  card-labels)
                (repeat :separator))))

(defn- bj-dialog
  [title dealer-hand player-hand actions hide-first-dealer-card]
  (-> (custom-dialog
        :modal? true
        :title title
        :content (vertical-panel 
                   :items [(horizontal-panel 
                             :items (player-hand-items dealer-hand hide-first-dealer-card))
                           (horizontal-panel 
                             :items (player-hand-items player-hand false))
                           (horizontal-panel
                             :items actions)]))
    pack!
    show!))

(defn busted-dialog
  ""
  ([dealer-hand player-hand player-name]
    (busted-dialog dealer-hand player-hand player-name true))
  
  ([dealer-hand player-hand player-name hide-first-dealer-card]
    (bj-dialog (str player-name " busted!")
               dealer-hand
               player-hand
               [(action :name "Busted!" :handler #(return-from-dialog % :busted))]
               hide-first-dealer-card)))

(defn player-dialog
  ""
  [dealer-hand player-hand player-name]
  (let [hit-act (action :name "Hit" :handler (fn [e] (return-from-dialog e :hit)))
        stand-act (action :name "Stand" :handler (fn [e] (return-from-dialog e :stand)))]
    (bj-dialog (str "Blackjack " player-name)
               dealer-hand
               player-hand
               [hit-act stand-act]
               true)))

(defn create-crappy-ui
  []
  (doto (JFrame. "Carioca")
    (.add (JLabel. "trolo"))
    (.setSize 400 400)
    (.setVisible true)))



