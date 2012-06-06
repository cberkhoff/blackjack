(ns carioca.game
  (:use clojure.set
        carioca.ui))

; facil
; (pr suits)
(def suits
  [:spades :hearts :diamonds :clubs])

; facil
; range
; conj
(def cards
  (conj (range 2 11) :ace :jack :queen :king))

; medio
; repeat
; zipmap
; mapcat
(def deck
  (mapcat #(zipmap cards (repeat %)) suits))

; (for [card g/cards suit g/suits] [card suit])

; dificil
(defn new-game
  "Creates a hashmap with the state of a starting game. It
requires the names of the players"
  [& players]
  (let [players (conj players :dealer)]
    (let [n (count players)
        d (shuffle deck)]
	    (let [pd (partition 2 d)]
	      {:pile (apply concat (nthrest pd n))
	       :players (zipmap players (take n pd))})))) 





; medio
(defn human-players
  [game]
  (filter string? (keys (:players game))))

; facil
; :keyword is fn
(defn player-hand
  [game player-name]
  ((:players game) player-name))

; facil
; usar otra funcion
(defn dealer-hand
  [game]
  (player-hand game :dealer))

; complejo
(defn hand-points
  [hand]
  (let [v (map first hand)]
    (let [n-aces (count (filter #(= :ace %) v))]
      (let [easy-points (+ (reduce + (filter number? v))
                           (* 10 (count (filter #(contains? #{:king :queen :jack} %) v))))]
        (let [base (+ easy-points n-aces)]
          (+ base (* 10 (min (quot (- 21 base) 10) n-aces))))))))        

;(hand-points [[:king] [2] [3] [4]]) ;19
;(hand-points [[:ace] [:ace] [:ace] [4]]) ;17
;(hand-points [[:ace] [:king] [3] [4]]) ;18
;(hand-points [[:ace] [4] [3] [4]]) ;12
;(hand-points [[:ace] [3] [3] [4]]) ;21
;(hand-points [[:ace] [:king] [:king] [:ace]]) ; 22

; medio
; set
; map
(defn hand-to-set
  [hand]
  (set (map first hand)))

(hand-to-set (player-hand the-game "imella"))

; medio
; let
; not-empty?
; intersection
(defn has-card
  [hand cards]
  (let [hand (hand-to-set hand)]
    (not-empty (intersection cards hand))))

;(has-card (player-hand the-game "imella") #{8})

; medio
; when
; and
(defn has-natural?
  [hand]
  (when (and (= (count hand) 2)
           (has-card hand #{:ace})
           (has-card hand #{:king :queen :jack 10}))
    hand))

; facil
(defn busted?
  [hand]
  (> (hand-points hand) 21))

; complejo
; update-in
(defn hit
  [game player-name]
  (-> game
    (update-in [:pile] rest)
    (update-in [:players player-name] conj (first (:pile game)))))


; complejo
(defn play-dealer-turn
  [game]
  (if (has-natural? (dealer-hand game))
    (for [player-name (human-players game)]
      (if (has-natural? (player-hand game player-name))
        (do
          (dealer-dialog (dealer-hand game))
          game)
        (do 
          (busted-dialog (dealer-hand game)
                         (player-hand game player-name)
                         player-name
                         false)
          game)))
    (if (< (hand-points (dealer-hand game)) 17)
      (recur (hit game :dealer))
      (do
        (dealer-dialog (dealer-hand game))
        game)
      )))

;(play-dealer-turn (new-game))

; complejo
(defn play-player-turn
  [game player-name]
  (loop [game game]
    (if (= (play-dialog (dealer-hand game)
                          (player-hand game player-name)
                          player-name)
           :hit)
      (let [next-game (hit game player-name)]
        (if (busted? (player-hand next-game player-name))
           (do
             (busted-dialog (dealer-hand game)
                            (player-hand next-game player-name) 
                            player-name)
             next-game)
          (recur next-game)))
      game)))

;(play-player-turn the-game "imella")

; facil (y bonito)
(defn play-players-turns
  [game]
  (reduce play-player-turn game (human-players game)))

;(play-players-turns the-game)

; complejo
(defn scores
  [game]
  (map #(if (> (second %) 21)
          (conj % :busted)
          %) 
    (sort-by second 
             (let [ps (:players game)]
               (zipmap (keys ps) (map hand-points (vals ps)))))))

; facil
; se pueden sacar los parentesis :)
(defn play-game
  [game]
  (-> game
    (play-players-turns)
    (play-dealer-turn)
    (scores)))
