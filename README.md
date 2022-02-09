
# Minecraft Cultureland Plugin
### 문화상품권 후원 자동 처리 플러그인
- 문화상품권을 자동으로 충전하고 이에 따른 보상을 지급하는 플러그인입니다. 
- 모든 소스는 오픈소스로 공개했습니다. 
- 컬쳐랜드 직원분들의 심기가 불편하시면 없어질 수 있는 플러그인입니다.
- 개인 서버에서 사용할 예정으로 제작한 플러그인이였으나, 서버 제작이 파기되는 바람에 그냥 올립니다.


# Demo
플러그인이 동작하는 사진입니다.
![enter image description here](https://raw.githubusercontent.com/gooday2die/TorchDonation/main/pics/1.png)

명령어 `/donate 문화상품권` 을 이용해서 후원하는 사진입니다.

![enter image description here](https://raw.githubusercontent.com/gooday2die/TorchDonation/main/pics/3.png)
후원이 끝나고 보상이 지급되는 사진입니다.

# Features
- Spigot-1.18.1 에서 작동을 확인했습니다.
- 간단한 명령어를 통해서 문화상품권 후원을 처리할 수 있습니다.
- 모든 기능이 Asynchronous 하게 실행됩니다.
- 문화상품권 후원에 따라서 보상을 서버 관리자 마음대로 지정할 수 있습니다. 
- MySQL 서버를 사용하거나 SQLite 파일을 사용하는 것을 선택할 수 있습니다.

# Future Features
### 이미 다 개발된 사항이지만 이 플러그인으로 옮겨오지 않은 내용들입니다.
언제 옮겨올지는 모르겠지만, 일단 현재 개발된 내용들은 다음과 같습니다. 개발되면 여기에 체크 표시를 해놓겠습니다. 
- [ ] 서버 내부에서 사용한 문화상품권 핀번호를 재사용하지 못하게 방지합니다
- [ ] 후원 시도 횟수가 많이 틀리면 후원을 제한합니다
- [ ] 서버 후원을 어느정도 했는지에 대해서 보고합니다
- [ ] 서버 후원시 Broadcast 를 진행할지 여부를 판단합니다.
- [ ] 관리자에게 알리는 기능
- [ ] 서버 후원 보고 기능
- [ ] 서버 후원 페이지 기능

# Installation
1. [Release](https://github.com/gooday2die/TorchDonation/releases/) 페이지에 가서 최신 버전을 다운받으시면 됩니다.
2. 서버의 `/plugin` 디렉토리에 다른 플러그인과 동일하게 넣어주시면 됩니다.
3. 서버를 `restart` 하거나 `reload`(권장되지 않음) 하여서 플러그인을 활성화하시면 됩니다.
4. `config.yml` 에 해당하는 내용을 수정하시면 됩니다.
5. 만일 플러그인이 실행되지 않는다면, Chrome이 설치되어있는지 확인해주세요.

# Commands
다음의 명령어들을 통해  서버에서 후원을 할 수 있습니다.

    /후원 1234-1234-1234-1234
    /문상 1234-1234-1234-1234
    /donate 1234-1234-1234-1234
    /redeem 1234-1234-1234-1234
# Permissions
다음의 Permission Node 를 통해 이 플러그인을 이용할 수 있습니다.
   - `TorchDonation.donate` : 위의 후원 명령어를 사용할 권한입니다.

# Config.yml
### Config.yml 예시입니다. 
Config.yml 설정법에 대한 질문과 이와 관련된 버그에 대한 질의에 대한 답변은 늦을 수 있습니다. 
```
# Torch Donation Plugin By Gooday2die
# https://github.com/gooday2die/TorchDonation
#
# config.yml에 적힌 내용 안읽고 질문하면 답변 안합니다.
# 사용시 라이센스 지우지 마시기 바랍니다.

# 컬쳐랜드 ID
username: "username"
# 컬쳐랜드 비밀번호
password: "password"
# 후원 성공시 실행할 명령어
# 명령어가 여러개일 경우 ## 로 구분합니다
# Placeholder가 있습니다.
# %AMOUNT% : 후원한 금액
# %USER% : 후원한 유저
# 예시: rewardCommands: "say %USER%##say %AMOUNT%"
# -> say 후원자 이름
# -> say 후원 금액
# 위 두개의 명령어 실행
rewardCommands: "say %USER%##say %AMOUNT%"
# MYSQL DB를 사용할지 여부입니다. DB가 뭔지 모르시면 false 로 놔주세요.
# 만약 MYSQL DB를 사용하지 않는다면 SQLITE3 으로 저장합니다
useMySQL: false
# MYSQL DB 의 IP 입니다
dbIP: ""
# MYSQL DB 의 username 입니다
dbID: ""
# MYSQL DB 의 비밀번호입니다
dbPW: ""
# MYSQL DB 의 DB 이름입니다
dbName: ""
# MYSQL DB 에서 사용할 prefix 이름입니다.
dbTablePrefix: ""

```
# Warning
- 플러그인을 사용하며 발생하는 모든 불이익을 포함한 책임은 사용자 본인에게 있습니다. 
- 플러그인에 대한 PR, Issue 들은 환영이나, config.yml 에 대한 질문과 PR, Issue는 답이 늦을 수 있습니다.
- Soruce Code 를 임의로 개조하는 행위는`GPL 3.0 License` 를 지키는 선 안에서 허용이 됩니다. 하지만 다음의 행위도 추가적으로 **금지**되어있습니다.
1. 플러그인 메세지의 Prefix를 바꾸는 행위: 가령 본인 서버에 적용시키고자 소스코드 뜯어서 임의로 컴파일 하며, **메세지 포멧 중 [TorchPlugin] 을 어떻게라도 훼손하는 경우**.
2. 본인의 플러그인인 것 처럼 우기고 배포하는 경우. 배포는 문제가 되지 않지만, **최소한 현재 보고 계신 웹사이트의 주소 정도는 적어주시기 바랍니다**. 
3. 플러그인을 **개조하여 임의 판매하는 경우** 
- Selenium을 사용하기에 메모리 사용량이 조금 많을 수 있습니다.
- Selenium과 WebDriverManager 때문에 CONSOLE 화면에 다음의 메세지가 나올 수 있습니다. 
```
Using chromedriver 97.0.4692.71 (resolved driver for Chrome 97)
Exporting webdriver.chrome.driver as C:\Users\pc\.cache\selenium\chromedriver\win32\97.0.4692.71\chromedriver.exe
```
- Selenium에서 ChromeDriver를 사용합니다. 서버 머신에 Chrome이 반드시 깔려있어야 이 플러그인을 실행할 수 있습니다.
- 
# Special Thanks To
### 플러그인을 개발하며 사용된 라이브러리입니다
- Selenium @ https://github.com/SeleniumHQ/selenium
- WebDriverManager @ https://github.com/bonigarcia/webdrivermanager
