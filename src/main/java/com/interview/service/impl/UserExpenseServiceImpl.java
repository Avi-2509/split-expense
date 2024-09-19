package com.interview.service.impl;

import com.interview.enums.ExpenseTypeEnum;
import com.interview.model.UserExpense;
import com.interview.service.UserExpenseService;

import java.util.List;

public class UserExpenseServiceImpl implements UserExpenseService {

    @Override
    public void showBalance(List<UserExpense> userExpenseList) {
        if(userExpenseList.isEmpty()){
            System.out.println("No balances");
            return;
        }

        for(UserExpense userExpense: userExpenseList){
            System.out.println(userExpense.getOwesUserId() + " owes " +
                    userExpense.getHelperUserId() + ": " + userExpense.getAmount());
        }
    }

    @Override
    public void showBalanceByUserId(final String userId, final List<UserExpense> userExpenseList) {
        if(userExpenseList.isEmpty()){
            System.out.println("No balances");
            return;
        }

        for(UserExpense userExpense: userExpenseList){
            if(userExpense.getOwesUserId().equals(userId)){
                System.out.println(userExpense.getOwesUserId() + " owes " +
                        userExpense.getHelperUserId() + ": " + userExpense.getAmount());
            }
        }

        for(UserExpense userExpense: userExpenseList){
            if(userExpense.getHelperUserId().equals(userId)){
                System.out.println(userExpense.getOwesUserId() + " owes " +
                        userExpense.getHelperUserId() + ": " + userExpense.getAmount());
            }
        }
    }


    @Override
    public List<UserExpense> addNewExpense(List<String> inputList, List<UserExpense> userExpenseList){
        String helperUserId = inputList.get(1);
        Double amount = Double.parseDouble(inputList.get(2));

        Integer numberOfOwes = Integer.parseInt(inputList.get(3));

        int expenseTypeEnumIdxInput = 3 + numberOfOwes + 1;
        ExpenseTypeEnum expenseTypeEnum = ExpenseTypeEnum.valueOf(inputList.get(expenseTypeEnumIdxInput));
        switch (expenseTypeEnum){
            case EQUAL:
                return splitEqually(helperUserId, amount, userExpenseList, numberOfOwes, inputList);

            case EXACT:
                return splitExactly(helperUserId, amount, userExpenseList, numberOfOwes, inputList, expenseTypeEnumIdxInput + 1);

            case PERCENT:
                return splitByPercent(helperUserId, amount, userExpenseList, numberOfOwes, inputList, expenseTypeEnumIdxInput + 1);
        }
        return userExpenseList;
    }

    private List<UserExpense> splitExactly(String helperUserId, Double amount, List<UserExpense> userExpenseList,
                                           Integer numberOfOwes, List<String> inputList, Integer expenseTypeEnumIdxInput) {
        int currSum = 0;
        int tmp = expenseTypeEnumIdxInput;

        for(int i = 0; i < numberOfOwes; i++){
            currSum += Integer.parseInt(inputList.get(tmp));
            tmp += 1;
        }

        System.out.println(currSum);
        if(currSum != amount){
            System.out.println("not able to add because provided split is not same as amount");
            return userExpenseList;
        }

        int idx = 4;
        for(int i = 0; i < numberOfOwes; i++){
            if(helperUserId.equals(inputList.get(idx))){
                continue;
            }

            UserExpense userExpense = new UserExpense();
            userExpense.setOwesUserId(inputList.get(idx));
            userExpense.setHelperUserId(helperUserId);
            userExpense.setAmount(Double.parseDouble(inputList.get(expenseTypeEnumIdxInput)));

            UserExpense alreadyOwes = checkIfAlreadyOwesMoney(inputList.get(idx), helperUserId, userExpenseList);
            if(alreadyOwes != null){
                alreadyOwes.setAmount(alreadyOwes.getAmount() + userExpense.getAmount());
                userExpenseList.set(userExpenseList.indexOf(alreadyOwes), alreadyOwes);
                idx += 1;
                expenseTypeEnumIdxInput += 1;
                continue;
            }

            UserExpense owesIsHelper = checkIfOwesIsHelper(inputList.get(idx), helperUserId, userExpenseList);
            if(owesIsHelper != null){
                if(owesIsHelper.getAmount() > userExpense.getAmount()){
                    userExpense.setAmount(owesIsHelper.getAmount() - userExpense.getAmount());
                    userExpenseList.set(userExpenseList.indexOf(owesIsHelper), owesIsHelper);
                    expenseTypeEnumIdxInput += 1;
                    idx += 1;
                    continue;
                }
                else{
                    userExpense.setAmount(userExpense.getAmount() - owesIsHelper.getAmount());
                    userExpenseList.remove(owesIsHelper);
                }
            }

            // if user
            userExpenseList.add(userExpense);
            idx += 1;
            expenseTypeEnumIdxInput += 1;
        }
        return userExpenseList;
    }

    private List<UserExpense> splitEqually(String helperUserId, Double amount, List<UserExpense> userExpenseList, Integer numberOfOwes,
                                           List<String> inputList) {
        Double equalAmount = amount / numberOfOwes;
        int idx = 4;
        for(int i = 0; i < numberOfOwes; i++){
            if(helperUserId.equals(inputList.get(idx))){
                idx += 1;
                continue;
            }
            UserExpense userExpense = new UserExpense();
            userExpense.setOwesUserId(inputList.get(idx));
            userExpense.setHelperUserId(helperUserId);
            userExpense.setAmount(equalAmount);

            UserExpense alreadyOwes = checkIfAlreadyOwesMoney(inputList.get(idx), helperUserId, userExpenseList);
            if(alreadyOwes != null){
                alreadyOwes.setAmount(alreadyOwes.getAmount() + userExpense.getAmount());
                userExpenseList.set(userExpenseList.indexOf(alreadyOwes), alreadyOwes);
                idx += 1;
                continue;
            }

            UserExpense owesIsHelper = checkIfOwesIsHelper(inputList.get(idx), helperUserId, userExpenseList);
            if(owesIsHelper != null){
                if(owesIsHelper.getAmount() > userExpense.getAmount()){
                    userExpense.setAmount(owesIsHelper.getAmount() - userExpense.getAmount());
                    userExpenseList.set(userExpenseList.indexOf(owesIsHelper), owesIsHelper);
                    idx += 1;
                    continue;
                }
                else{
                    userExpense.setAmount(userExpense.getAmount() - owesIsHelper.getAmount());
                    userExpenseList.remove(owesIsHelper);
                }
            }

            // if user
            userExpenseList.add(userExpense);
            idx += 1;
        }
        return userExpenseList;
    }

    private List<UserExpense> splitByPercent(String helperUserId, Double amount, List<UserExpense> userExpenseList, Integer numberOfOwes,
                                           List<String> inputList,  Integer expenseTypeEnumIdxInput) {
        int currPercent = 0;
        int tmp = expenseTypeEnumIdxInput;
        for(int i = 0; i < numberOfOwes; i++){
            currPercent += Integer.parseInt(inputList.get(tmp));
            tmp += 1;
        }

        if(currPercent != 100){
            System.out.println("not able to add because provided split percent is not 100");
            return userExpenseList;
        }

        int idx = 4;
        for(int i = 0; i < numberOfOwes; i++){
            if(helperUserId.equals(inputList.get(idx))){
                continue;
            }
            Double splitAmount = amount * (Integer.parseInt(inputList.get(expenseTypeEnumIdxInput))) / 100;
            UserExpense userExpense = new UserExpense();
            userExpense.setOwesUserId(inputList.get(idx));
            userExpense.setHelperUserId(helperUserId);
            userExpense.setAmount(splitAmount);

            UserExpense alreadyOwes = checkIfAlreadyOwesMoney(inputList.get(idx), helperUserId, userExpenseList);
            if(alreadyOwes != null){
                alreadyOwes.setAmount(alreadyOwes.getAmount() + userExpense.getAmount());
                userExpenseList.set(userExpenseList.indexOf(alreadyOwes), alreadyOwes);
                idx += 1;
                expenseTypeEnumIdxInput += 1;
                continue;
            }

            UserExpense owesIsHelper = checkIfOwesIsHelper(inputList.get(idx), helperUserId, userExpenseList);
            if(owesIsHelper != null){
                if(owesIsHelper.getAmount() > userExpense.getAmount()){
                    userExpense.setAmount(owesIsHelper.getAmount() - userExpense.getAmount());
                    userExpenseList.set(userExpenseList.indexOf(owesIsHelper), owesIsHelper);
                    expenseTypeEnumIdxInput += 1;
                    idx += 1;
                    continue;
                }
                else{
                    userExpense.setAmount(userExpense.getAmount() - owesIsHelper.getAmount());
                    userExpenseList.remove(owesIsHelper);
                }
            }

            // if user
            userExpenseList.add(userExpense);
            expenseTypeEnumIdxInput += 1;
            idx += 1;
        }
        return userExpenseList;
    }


    private UserExpense checkIfAlreadyOwesMoney(final String owesUserid, String helperUserId, final List<UserExpense> userExpenseList){
        for(UserExpense userExpense: userExpenseList){
            if(userExpense.getOwesUserId().equals(owesUserid) && userExpense.getHelperUserId().equals(helperUserId)){
                return userExpense;
            }
        }
        return null;
    }

    private UserExpense checkIfOwesIsHelper(final String owesUserid, String helperUserId, final List<UserExpense> userExpenseList){
        for(UserExpense userExpense: userExpenseList){
            if(userExpense.getHelperUserId().equals(owesUserid) && userExpense.getOwesUserId().equals(helperUserId)){
                return userExpense;
            }
        }
        return null;
    }
}
