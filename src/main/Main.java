package main;

import dao.MemberDAO;
import domain.Member;
import service.MemberService;
import util.InputUtil;
import util.PrintUtil;

import java.util.Scanner;

public class Main {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            MemberService memberService = new MemberService();

            while (true) {
                PrintUtil.printFirstMenu();
                int choice = InputUtil.getIntInput(sc);

                if (choice == 1) {
                    memberService.signup();
                } else if (choice == 2) {
                    Member member = memberService.login();
                    if (member != null) {
                        MainMenu mainMenu = new MainMenu(sc, member);
                        mainMenu.showMenu();
                    }
                } else if (choice == 3) {
                    System.out.println("이용해 주셔서 갑사합니다.");
                    break;
                }
            }
        }
}