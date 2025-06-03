# Java 17（alpine は軽量だけど native-image 等を使わないなら non-alpine の方が安心）
FROM eclipse-temurin:17-jdk

# 作業ディレクトリ
WORKDIR /app

# Maven Wrapperと依存ファイルだけ先にコピーしてキャッシュ活用
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# ✅ mvnwに実行権限を付与（← これが必要！）
RUN chmod +x mvnw

# 依存関係だけ先にダウンロードしてキャッシュ活用
RUN ./mvnw dependency:go-offline

# アプリのソースコードをコピー
COPY src src

# JARファイルをビルド（テストはスキップ）
RUN ./mvnw package -DskipTests

# Spring Bootアプリは8080番で起動する
EXPOSE 8080

# アプリを起動（← jar名を正確に書く！）
CMD ["java", "-jar", "target/todo-app-0.0.1-SNAPSHOT.jar"]
