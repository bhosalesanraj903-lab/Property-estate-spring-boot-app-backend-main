# Loan Default Early-Warning System
### Ranking 225K real loans by expected dollar loss — calibrated, leakage-controlled, and built to drive a credit team's review queue



---

## What this project does

Most credit-risk projects stop at "my model has 0.72 AUC." A lending team can't act on that. They have limited review capacity and need to know one thing: **which loans, ranked by how much money they'll actually cost us, should a human review first?**

This project answers that. It turns ~1.3 million historical Lending Club loans (2007–2018) into a **prioritized, dollar-ranked watchlist** with an interactive dashboard, where every figure is a genuine, calibrated estimate — not an inflated score.

**Headline results (out-of-sample, 2016–2018 test set):**
- **$471M** total calibrated expected loss across 225,639 scored loans
- Reviewing the **riskiest 10%** of loans surfaces **~34%** of all expected dollar loss
- A focused **Critical tier of ~2,150 loans** — a review queue small enough to actually work through

---

## Why it's built differently

| Typical version | This project |
|---|---|
| Predicts default, reports accuracy | Ranks loans by **expected dollar loss** (PD x exposure x LGD) |
| Random train/test split | **Time-based split** — trained on 2007–2015, tested on 2016–2018 |
| Uses every column | **Leakage-controlled** — only features known at application time |
| Reports raw model probabilities | **Isotonic-calibrated** so probabilities are trustworthy |
| Ends in a notebook | Ships an **interactive risk dashboard** for non-technical stakeholders |

---

## The two decisions that make it honest

**1. Leakage control.** Lending Club's data includes columns — total payments, recoveries, last payment date — that only exist *after* a loan performs. Training on them produces a fake 0.95+ AUC that collapses in production. I stripped the data to only what a lender knows *at application time*, which is why the AUC is a realistic 0.72.

**2. Calibration.** A calibration check revealed the model was over-predicting default risk by roughly 2x (it said 50% when reality was 22%) — a side effect of using balanced class weights to improve ranking. I corrected it with isotonic calibration, which brought predicted and actual default rates into line and dropped the total expected-loss estimate from an inflated $1.03B to a genuine $471M. This is what makes the dollar figures defensible.

---

## Key finding

Per-loan risk climbs steadily from grade A to G — but **total expected loss peaks at grade C**, because that's where loan volume concentrates. Total loss is risk x volume, so the largest exposure sits in the middle grades, not the worst ones. That's the kind of insight a portfolio manager acts on, and it only emerges once the dollar framing is in place.

---

## Pipeline

```
accepted_2007_to_2018Q4.csv  (2.2M rows, 151 cols)
        |
   clean + drop empty/leakage columns        ->  1.3M rows, 26 features
   format fixes, missing-value handling
   outlier capping (income, DTI, utilization)
        |
   time-based split (train <2016 | test >=2016)
        |
   HistGradientBoosting + isotonic calibration
        |
   expected loss = PD x loan_amnt x LGD(0.65)
        |
   risk tiers + dollar-ranked watchlist  ->  interactive dashboard
```

---

## Tech stack

Python · pandas · scikit-learn (HistGradientBoosting, CalibratedClassifierCV) · interactive HTML/Chart.js dashboard

---

## What I'd build next

- Tune risk-tier cutoffs to the calibrated score distribution
- Reject-inference using the rejected-loans file to correct selection bias
- A champion/challenger setup to monitor model drift over time
- Tie the review cutoff to actual reviewer cost so it optimizes net dollars saved

---

## Data

Lending Club accepted loans, 2007–2018 (CC0 / public domain). Full dataset (~1.3M loans after filtering) available on Kaggle: https://www.kaggle.com/datasets/wordsforthewise/lending-club . A scored sample is included in this repo.

---

*Built by **[Your Name]**. Portfolio project — not production credit-decisioning software.*
