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
[`connected`](doc/connecting.md#%E6%8E%A5%E7%B6%9A%E7%A2%BA%E7%AB%8B%E5%BE%85%E3%81%A1%E7%8A%B6%E6%85%8B)
#### クライアント→サーバー
プレイヤー名(String)、アプリケーションID(Integer)
### ロビー内
#### サーバー→クライアント
[`joinedLobby`](doc/in_lobby.md#joinedLobby)、[`roomList`](doc/in_lobby.md#roomList)、[`lobbyMember`](doc/in_lobby.md#lobbyMember)、[`leftLobby`](doc/in_lobby.md#leftLobby)、[`exit`](doc/in_lobby.md#exit)、[`error`](doc/in_lobby.md#error)
#### クライアント→サーバー
[`join`](doc/in_lobby.md#join)、[`create`](doc/in_lobby.md#create)、[`roomList`](doc/in_lobby.md#roomList-1)、[`exit`](doc/in_lobby.md#exit-1)
### ルーム内
#### サーバー→クライアント
[`joinedRoom`](doc/in_room.md#joinedRoom)、[`roomMember`](doc/in_room.md#roomMember)、[`roomOption`](doc/in_room.md#roomOption)、[`tell`](doc/in_room.md#tell)、[`leftRoom`](doc/in_room.md#leftRoom)、[`exit`](doc/in_room.md#exit)、[`error`](doc/in_room.md#error)
#### クライアント→サーバー
[`broad`](doc/in_room.md#broad)、[`tell`](doc/in_room.md#tell-1)、[`roomMember`](doc/in_room.md#roomMember)、[`roomOption`](doc/in_room.md#roomOption)、[`leave`](doc/in_room.md#leave)、[`exit`](doc/in_room.md#exit-1)

## ライセンス
[GNU GPL v3](LICENSE)
## リンク
* [Document](doc/doument.md)
* [WYUN Library](https://github.com/ystt-lita/WYUN_Library)
