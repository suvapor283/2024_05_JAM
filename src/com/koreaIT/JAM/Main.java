package com.koreaIT.JAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.JAM.dto.Article;

public class Main {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		List<Article> articles = new ArrayList<>();
		
		int lastArticleId = 1;
		
		System.out.println("== 프로그램 시작 ==");
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();
			
			if (cmd.equals("exit")) {
				break;
			}
			
			if (cmd.equals("article write")) {
				System.out.println("== 게시물 작성 ==");
				
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				
				Article article = new Article(lastArticleId, title, body);
				
				articles.add(article);
				
				System.out.printf("%d번 게시물이 작성되었습니다\n", lastArticleId);
				
				lastArticleId++;
			} else if (cmd.equals("article list")) {
				System.out.println("== 게시물 목록 ==");
				
				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다");
					continue;
				}
				
				System.out.println("번호	|	제목");
				for (int i = articles.size() - 1; i >= 0; i--) {
					Article article = articles.get(i);
					
					System.out.printf("%d	|	%s\n", article.id, article.title);
				}
				
			}
		}
		
		sc.close();
		
		System.out.println("== 프로그램 끝 ==");
		
	}
}