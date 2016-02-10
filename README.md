#Clojush-with-gp-guidance
Eddie's Hampshire College CS-254 Final

Clojush:
https://github.com/lspector/Clojush

The goal of this project is to have the Clojush GP system find the best (problem specific) probabilities for all genetic operators. It does this by recording the change in average error between generations, and moves each operator probability higher or lower by a learning rate value based on which probabilities had the best improvement between generations. 

The reason I chose to explore this idea is because of the fact that stronger mutations can be helpful towards the beginning of evolution, but becomes more harmful as a solution is approached. This system is intended to intelligently change how frequently all operatation are used, in a way is best for evolution.

Based on a number of tests, using some of Clojush's examples, it doesn't seem like this particular implementation has much on an effect. Significant changes in the probability of each genetic operator only happened in problems that took many generations to solve.



##Files I Created

gpGuidance / error_and_gp_operator_recording.clj

gpGuidance / gp_operator_guiding.clj

##Files I Modified To

pushgp / pushgp.clj

  - Added :gp-guidance-learning-rate to the argmap
    - Used to dermine how much to tweak the propability of each genetic operator.
  
  - Added modified version of pushgp function called pushgp-with-gp-guidance
    - Same as Clojush's gp function, except it tweaks the propbability of each genetic operator after each generation.
  
pushgp / report.clj

  - Added modified version of report-and-check-for-success called report-and-check-for-success-with-gp-guidance
