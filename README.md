Eddie's Hampshire College CS-254 Final

Clojush:
https://github.com/lspector/Clojush

The goal of this project is have the Clojush GP system find the best (problem specific) probabilities for all genetic operators. It does this by recording the change in average error between generations, and moves each operator probability higher or lower by a learning rate value based on which probabilites had the best improvment between generations.

Files I Created
===============
gpGuidance / error_and_gp_operator_recording.clj
gpGuidance / gp_operator_guiding.clj

Files I Added To
================
pushgp / pushgp.clj
  -Added :gp-guidance-learning-rate to the argmap
  -Added modified version of pushgp function called pushgp-with-gp-guidance
  
pushgp / report.clj
  -Added modified version of report-and-check-for-success called report-and-check-for-success-with-gp-guidance
