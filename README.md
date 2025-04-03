# Receivables and Payables Register

The project was created with the aim of improving work efficiency at my previous position by developing a Receivables
and Payables Register that is automatically updated. Its purpose is to assist individuals responsible for entering
documents into the system and supervising their proper processing.

### The application includes several key features:

* Generating various reports to support analytical work and enable better data management.

* Grouping payment due dates for improved monitoring of payables and receivables.

Automating this process will save time and increase work efficiency while ensuring greater accuracy in data processing.

#### Example to use PatchMapping:

```courseignore
[
    {
        "op":"replace", 
        "path":"/contractorName",
        "value":"XYZ"
    }
]
```

#### Available reports:

1. Overdue receivables report - show receivables that have not been paid on time

*/api/receivables/overdue*

2. Overdue receivables group by contactors report - show receivables that have not been paid on time but group by
   contactor name and sum up all unpaid invoices

*/api/receivables/overdue/grouped*

3. Aging report - sort receivables bu due date, helping to assess the risk of counterparty default.

*api/receivables/aging*


4. Overdue payables report - show payables that have not been paid on time

*/api/payables/overdue*

5. Overdue payables group by contactors report - show payables that have not been paid on time but group by
   contactor name and sum up all unpaid invoices

*/api/payables/overdue/grouped*

6. Aging report - sort payables bu due date, helping to assess the risk of counterparty default.

*api/payables/aging*
