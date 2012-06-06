(ns blackjack.ui
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

(defn user-label
  [user-name]
  (label :text user-name
         :border 10
         :foreground :white
         :font {:name "ARIAL" :style :bold :size 14}))

(defn card-label
  [card]
  (label :icon (card-icon card)
         :border 10))

(defn card-button
  [card]
  (button :icon (card-icon card)))

(defn player-hand-items
  [player-hand hide-first-card]
  (let [card-labels (vec (map card-label player-hand))]
    (if hide-first-card
      (cons (card-label [:back :red]) (rest card-labels))
      card-labels)))

(defn- bj-dialog
  [title vertical-panel-items]
  (-> (custom-dialog
        :modal? true
        :title title
        :content (vertical-panel 
                   :items vertical-panel-items
                   :background "#A4A663"))
    pack!
    show!))

(defn- player-dialog
  [title dealer-hand player-name player-hand actions hide-first-dealer-card]
  (bj-dialog title 
             
               [(horizontal-panel 
                   :items (cons (user-label "dealer") (player-hand-items dealer-hand hide-first-dealer-card)) 
                   :background "#A4A663")
                 (horizontal-panel 
                   :items (cons (user-label player-name) (player-hand-items player-hand false))
                   :background "#A4A663")
                 (horizontal-panel
                   :items actions
                   :background "#A4A663")]
               ))

(defn dealer-dialog
  "Shows a dialog with the dealers hand and an ok button (returns :ok)."
  [dealer-hand]
  (bj-dialog "Dealer's hand"
             [(horizontal-panel 
                :items (cons (user-label "dealer") (player-hand-items dealer-hand false))
                :background "#A4A663")
              (horizontal-panel
                :items [(action :name "Ok" :handler #(return-from-dialog % :ok))]
                :background "#A4A663")]))

(defn busted-dialog
  "Shows a dialog with the dealer's, and the player's hand and an 
busted button (returns :busted)."
  ([dealer-hand player-hand player-name]
    (busted-dialog dealer-hand player-hand player-name true))
  
  ([dealer-hand player-hand player-name hide-first-dealer-card]
    (player-dialog (str "Blackjack - " player-name " busted!")
               dealer-hand
               player-name
               player-hand
               [(action :name "Busted!" :handler #(return-from-dialog % :busted))]
               hide-first-dealer-card)))

(defn play-dialog
  "Shows a dialog with the dealer's, and the player's hand and a 
hit (returns :hit) and stand (returns :stand) button."
  [dealer-hand player-hand player-name]
  (let [hit-act (action :name "Hit" :handler (fn [e] (return-from-dialog e :hit)))
        stand-act (action :name "Stand" :handler (fn [e] (return-from-dialog e :stand)))]
    (player-dialog (str "Blackjack - " player-name " turn")
               dealer-hand
               player-name
               player-hand
               [hit-act stand-act]
               true)))

(defn create-crappy-ui
  []
  (doto (JFrame. "Carioca")
    (.add (JLabel. "trolo"))
    (.setSize 400 400)
    (.setVisible true)))



