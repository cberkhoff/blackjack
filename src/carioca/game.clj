(ns carioca.game
  (:use clojure.set
        carioca.ui))

(def suits
  [:spades :hearts :diamonds :clubs])

(def cards
  (conj (range 2 11) :ace :jack :queen :king))

(def deck
  (mapcat #(into [] (zipmap cards (repeat %))) suits))

; implementar despues
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

(def the-game (new-game "nigro" "imella")) 

(:players the-game)
(:pile the-game)

(defn human-players
  [game]
  (filter string? (keys (:players game))))


(human-players the-game)

(defn player-hand
  [game player-name]
  ((:players game) player-name))

(defn dealer-hand
  [game]
  (player-hand game :dealer))

(player-hand the-game "imella")
(dealer-hand the-game)

(defn point-posibilities
  [hand]
  (letfn [(pp
            [hand current branches]
            (if (empty? hand)
              branches
              (let []
                (if (number? current))
              (let [next (first hand)
                    r-hand (rest hand)
                    next-branch (map #(+ % current) branches)]            
                (if (= next :ace)
                  (concat 
                    (pp r-hand 1 next-branch)
                    (pp r-hand 11 next-branch))
                  (recur r-hand (if (number? next) next 10) next-branch))))))]
         (pp hand (get (first hand) 0) [0])))

(defn hand-points
  [hand]
  (let [v (map first hand)]
    (let [n-aces (count (filter #(= :ace %) v))]
      (let [easy-points (+ (reduce + (filter number? v))
                           (* 10 (count (filter #(contains? #{:king :queen :jack} %) v))))]
        (let [base (+ easy-points n-aces)]
          (+ base (* 10 (min (quot (- 21 base) 10) n-aces))))))))        

(hand-points [[:king] [2] [3] [4]]) ;19
(hand-points [[:ace] [:ace] [:ace] [4]]) ;17
(hand-points [[:ace] [:king] [3] [4]]) ;18
(hand-points [[:ace] [4] [3] [4]]) ;12
(hand-points [[:ace] [3] [3] [4]]) ;21
(hand-points [[:ace] [:king] [:king] [:ace]]) ; 22

(defn hand-to-set
  [hand]
  (set (map first hand)))

(hand-to-set (player-hand the-game "imella"))

(defn has-card
  [hand cards]
  (let [hand (hand-to-set hand)]
    (not-empty (intersection cards hand))))

(has-card (player-hand the-game "imella") #{8})

(defn has-natural?
  [hand]
  (when (and (= (count hand) 2)
           (has-card hand #{:ace})
           (has-card hand #{:king :queen :jack 10}))
    hand))

(has-natural? [[:ace :spades] [:king :spades] [:queen :hearts]])
(has-natural? [[:ace :spades] [7 :spades] [3 :hearts]])
(has-natural? [[:ace :spades] [10 :spades]])

(defn busted?
  [hand]
  (> (hand-points hand) 21))

(defn hit
  [game player-name]
  (-> game
    (update-in [:pile] rest)
    (update-in [:players player-name] conj (first (:pile game)))))

(hit the-game "imella")

(player-dialog (dealer-hand the-game) 
               (player-hand the-game "imella") 
               "imella")

(busted-dialog (dealer-hand the-game)
               (player-hand the-game "imella") 
               "imella")

(busted-dialog (dealer-hand the-game)
               (player-hand the-game "imella")
               "imella"
               false)

(defn play-dealer-turn
  [game]
  (if (has-natural? (dealer-hand game))
    (for [player-name (human-players game)]
      (if (has-natural? (player-hand game player-name))
        game
        (do 
          (busted-dialog (dealer-hand game)
                       (player-hand game player-name)
                       player-name
                       false)
          game)))
    (if (< (hand-points (dealer-hand game)) 17)
      (recur (hit game :dealer))
      game
      )))

(play-dealer-turn the-game)

(defn play-player-turn
  [game player-name]
  (loop [game game]
    (if (= (player-dialog (dealer-hand game)
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

(play-player-turn the-game "imella")

(defn play-players-turns
  [game]
  (reduce play-player-turn game (human-players game)))

(play-players-turns the-game)

(defn scores
  [game]
  (map #(if (> (second %) 21)
          (conj % :busted)
          %) 
    (sort-by second 
             (let [ps (:players game)]
               (zipmap (keys ps) (map hand-points (vals ps)))))))

(scores the-game)

(defn play-game
  [game]
  (-> game
    (play-players-turns)
    (play-dealer-turn)
    (scores)))

(play-game the-game)
(play-game (new-game "clojure-boy" "dr-lazy" "mr-eager"))