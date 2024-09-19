package com.interview;

import com.interview.model.User;
import com.interview.model.UserExpense;
import com.interview.service.UserExpenseService;
import com.interview.service.UserService;
import com.interview.service.impl.UserExpenseServiceImpl;
import com.interview.service.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

public class InterviewApplication {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		UserExpenseService userExpenseService = new UserExpenseServiceImpl();

		List<UserExpense> userExpenseList = new ArrayList<>();
		List<User> userList = createUsers();

		while (true){
			String input = scanner.nextLine();
			List<String> inputList = Arrays.asList(input.split(" "));
			ActionTypeEnum actionTypeEnum = ActionTypeEnum.valueOf(inputList.get(0));

			switch (actionTypeEnum){
				case SHOW:
					if(inputList.size() > 1){
						userExpenseService.showBalanceByUserId(inputList.get(1), userExpenseList);
						break;
					}
					userExpenseService.showBalance(userExpenseList);
					break;

				case EXPENSE:
					userExpenseList = userExpenseService.addNewExpense(inputList, userExpenseList);
//					userExpenseService.showBalance(userExpenseList);
					break;
			}
		}
	}

	private static List<User> createUsers() {
		UserService userService = new UserServiceImpl();
		List<User> userList = new ArrayList<>();
		userService.createUserAndAdd("u1", "avi", "avi@gmail.com", "8994933", userList);
		userService.createUserAndAdd("u2", "avi", "avi@gmail.com", "8994933", userList);
		userService.createUserAndAdd("u3", "avi", "avi@gmail.com", "8994933", userList);
		userService.createUserAndAdd("u4", "avi", "avi@gmail.com", "8994933", userList);
		return userList;
	}

}
