# WYUN Server
Javaで書かれたゲーム用メッセージサーバーです。アプリケーションごとに分離されたロビーと、各ロビーのルーム管理、ルーム内でのクライアント間メッセージングを提供します。

## 使い方
### 前提
実行環境として、java 8以上が必要です。環境に合わせ最新のものをインストールしてください。
### インストール
適当な場所にwyun.jarを配置すればOKです。特にフォルダ構成に指定はありません。
### 実行
サーバーの起動方法は以下のコマンドです。
```terminal
$ java -jar wyun.jar Server
```
ポート8929でTCPソケットが開きます。  
動作確認用にクライアントが用意されています。
```terminal
$ java -jar wyun.jar App
```
localhostのポート8929に自動で接続します。他のデバイス上で動くサーバーに接続することはできません。
## 通信内容
サーバーは受信するメッセージがCRLF`\r\n`で区切られているとassumeします。  
### クライアントからサーバーへの接続
#### サーバー→クライアント
[`connected`]()
#### クライアント→サーバー
プレイヤー名(String)、アプリケーションID(Integer)
### ロビー内
#### サーバー→クライアント
[`joinedLobby`]()、[`roomList`]()、[`lobbyMember`]()、[`leftLobby`]()、[`exit`]()
#### クライアント→サーバー
[`join`]()、[`create`]()、[`roomList`]()、[`exit`]()
### ルーム内
#### サーバー→クライアント
[`joinedRoom`]()、[`roomMember`]()、[`roomOption`]()、[`leftRoom`]()、[`exit`]()
#### クライアント→サーバー
[`broad`]()、[`tell`]()、[`roomMember`]()、[`roomOption`]()、[`leave`]()、[`exit`]()

## links
* [Wiki]()
* [WYUN Library]()
* a