package service;

import dao.MemberDAO;
import domain.Member;

import java.util.Scanner;

public class MemberService {
    private final Scanner sc;
    private final MemberDAO dao;

    public MemberService() {
        this.sc = new Scanner(System.in);
        this.dao = new MemberDAO();
    }

    public void signup() {
        System.out.println("[회원가입]");

        System.out.print("회원 유형 (1: 사용자, 2: 관리자): ");
        int type = sc.nextInt();
        sc.nextLine(); // 개행 제거

        System.out.print("아이디: ");
        String id = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();
        System.out.print("나이: ");
        int age = sc.nextInt();
        sc.nextLine(); // 개행 제거

        String role = (type == 2) ? "ADMIN" : "USER";

        boolean ok = dao.createMember(new Member(id, password, age, role));
        if (ok) {
            System.out.println("회원가입 완료! 권한: " + role);
        } else {
            System.out.println("회원가입 실패.");
        }
    }

    public Member login() {
        System.out.println("[로그인]");
        while (true) {
            System.out.print("아이디: ");
            String id = sc.nextLine();
            System.out.print("비밀번호: ");
            String password = sc.nextLine();

            Member mem = dao.loginMember(id, password);
            if (mem != null) {
                System.out.println("로그인 성공!");
                return mem;
            }
            System.out.println("로그인 실패. 다시 시도해주세요.");
        }
    }
}