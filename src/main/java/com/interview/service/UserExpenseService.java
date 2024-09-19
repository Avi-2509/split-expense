package com.interview.service;

import com.interview.model.UserExpense;

import java.util.List;

public interface UserExpenseService {
    void showBalance(final List<UserExpense> userExpenseList);
    void showBalanceByUserId(final String userId, List<UserExpense> userExpenseList);

    List<UserExpense> addNewExpense(List<String> inputList, List<UserExpense> userExpenseList);
}
