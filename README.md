# WYUN Server
Javaで書かれたゲーム用メッセージサーバーです。アプリケーションごとに分離されたロビーと、各ロビーのルーム管理、ルーム内でのクライアント間メッセージングを提供します。

## 使い方
### 前提
実行環境として、java 14以上が必要です。環境に合わせ最新のものをインストールしてください。
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
[`connected`](../../wiki/接続確立待ち状態)
#### クライアント→サーバー
プレイヤー名(String)、アプリケーションID(Integer)
### ロビー内
#### サーバー→クライアント
[`joinedLobby`](../../wiki/ロビー内状態#joinedLobby)、[`roomList`](../../wiki/ロビー内状態#roomList)、[`lobbyMember`](../../wiki/ロビー内状態#lobbyMember)、[`leftLobby`](../../wiki/ロビー内状態#leftLobby)、[`exit`](../../wiki/ロビー内状態#exit)、[`error`](../../wiki/ロビー内状態#error)
#### クライアント→サーバー
[`join`](../../wiki/ロビー内状態#join)、[`create`](../../wiki/ロビー内状態#create)、[`roomList`](../../wiki/ロビー内状態#roomList-1)、[`exit`](../../wiki/ロビー内状態#exit-1)
### ルーム内
#### サーバー→クライアント
[`joinedRoom`](../../wiki/ルーム内状態#joinedRoom)、[`roomMember`](../../wiki/ルーム内状態#roomMember)、[`roomOption`](../../wiki/ルーム内状態#roomOption)、[`tell`](../../wiki/ルーム内状態#tell)、[`leftRoom`](../../wiki/ルーム内状態#leftRoom)、[`exit`](../../wiki/ルーム内状態#exit)、[`error`](../../wiki/ルーム内状態#error)
#### クライアント→サーバー
[`broad`](../../wiki/ルーム内状態#broad)、[`tell`](../../wiki/ルーム内状態#tell-1)、[`roomMember`](../../wiki/ルーム内状態#roomMember)、[`roomOption`](../../wiki/ルーム内状態#roomOption)、[`leave`](../../wiki/ルーム内状態#leave)、[`exit`](../../wiki/ルーム内状態#exit-1)
## 注意事項
このサーバープログラムには通信の保護手段やアクセス制御が特に実装されていません。  
公開インターネットにつながるコンピュータ上で実行した場合に生じるいかなる損害も作者は負うことができません。  
ご理解の上使用してください。
## ライセンス
[GNU GPL v3](LICENSE)
## リンク
* [Document](../../wiki)
* [WYUN Library](https://github.com/ystt-lita/WYUN_Library)
