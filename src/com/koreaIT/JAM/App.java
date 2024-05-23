package com.koreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.JAM.dto.Article;
import com.koreaIT.JAM.util.DBUtil;
import com.koreaIT.JAM.util.SecSql;

public class App {
	private final String URL = "jdbc:mysql://localhost:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
	private final String USER = "root";
	private final String PASSWORD = "";

	public void run() {

		System.out.println("== 프로그램 시작 ==");
		Scanner sc = new Scanner(System.in);

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					break;
				}

				if (cmd.equals("article write")) {
					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine().trim();
					System.out.printf("내용 : ");
					String body = sc.nextLine().trim();

					SecSql sql = new SecSql();
					sql.append("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?;", body);

					int id = DBUtil.insert(connection, sql);

					System.out.printf("%d번 게시물이 작성되었습니다\n", id);
				}

				else if (cmd.startsWith("article modify ")) {
					//

					try {
						int id = Integer.parseInt(cmd.split(" ")[2]);

						System.out.println("== 게시물 수정 ==");

						System.out.printf("수정할 제목 : ");
						String title = sc.nextLine().trim();
						System.out.printf("수정할 내용 : ");
						String body = sc.nextLine().trim();

						SecSql sql = new SecSql();
						sql.append("UPDATE article SET");
						sql.append("updateDate = NOW()");
						sql.append(", title = ?", title);
						sql.append(", `body` = ?", body);
						sql.append("WHERE id = ?;", id);

						DBUtil.update(connection, sql);
						System.out.printf("%d번 게시물이 수정되었습니다.%n", id);

					} catch (NumberFormatException e) {
						System.out.println("명령어를 다시 입력해주세요.");
						continue;
					}
				}

				else if (cmd.startsWith("article delete ")) {
					//
					try {
						int id = Integer.parseInt(cmd.split(" ")[2]);

						System.out.println("== 게시물 삭제 ==");

						SecSql sql = new SecSql();
						sql.append("DELETE FROM article");
						sql.append("WHERE id = ?;", id);

						DBUtil.delete(connection, sql);
						System.out.printf("%d번 게시물이 삭제되었습니다.%n", id);

					} catch (NumberFormatException e) {
						System.out.println("명령어를 다시 입력해주세요.");
						continue;
					}
				}

				else if (cmd.startsWith("article detail ")) {
					//
					try {
						int id = Integer.parseInt(cmd.split(" ")[2]);

						System.out.println("== 게시물 조회 ==");

						List<Article> articles = new ArrayList<>();

						SecSql sql = new SecSql();
						sql.append("SELECT * FROM article");
						sql.append("ORDER BY id DESC;");

						List<Map<String, Object>> articleListMap = DBUtil.selectRows(connection, sql);

						for (Map<String, Object> articleMap : articleListMap) {
							articles.add(new Article(articleMap));
						}

						if (articles.isEmpty()) {
							System.out.println("게시물이 존재하지 않습니다");
							continue;
						}

						for (Article article : articles) {
							if (article.id == id) {
								System.out.println("	번호	|	     작성일		|     제목	|	내용");
								System.out.printf("	%d	|	%s	|     %s 	|	%s%n", article.id, article.regDate,
										article.title, article.body);
							}
						}
					} catch (NumberFormatException e) {
						System.out.println("명령어를 다시 입력해주세요.");
						continue;
					}
				}

				else if (cmd.equals("article list")) {

					List<Article> articles = new ArrayList<>();

					SecSql sql = new SecSql();
					sql.append("SELECT * FROM article");
					sql.append("ORDER BY id DESC;");

					List<Map<String, Object>> articleListMap = DBUtil.selectRows(connection, sql);

					for (Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}

					if (articles.isEmpty()) {
						System.out.println("게시물이 존재하지 않습니다");
						continue;
					}

					System.out.println("== 게시물 목록 ==");
					System.out.println("	번호	|	제목	  |	    작성일	");

					for (Article article : articles) {
						System.out.printf("	%d	|	%s	  |	%s\n", article.id, article.title, article.regDate);
					}
				}

				else {
					System.out.println("존재하지 않는 명령어입니다.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		sc.close();
		System.out.println("== 프로그램 끝 ==");
	}
}