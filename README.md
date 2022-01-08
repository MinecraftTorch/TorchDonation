
# Minecraft Cultureland Plugin
### 한글 
[CulturelandPython](https://github.com/gooday2die/CulturelandPython) 을 활용하여 마인크래프트 서버에서 문화상품권 후원을 처리하는 플러그인입니다. **이 플러그인만 가지고 서버에서 문화상품권 처리를 할 수 없습니다!**

# Demo
플러그인이 동작하는 영상입니다. 클릭시 유튜브로 연결됩니다.

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/p_wB6YucMz0/0.jpg)](https://www.youtube.com/watch?v=p_wB6YucMz0)


# Features
- Spigot-1.18.1 에서 작동을 확인했습니다.
- 간단한 명령어를 통해서 문화상품권 후원을 처리할 수 있습니다.
- Asynchronous 하게 동작하여 서버에 부하를 많이 주지 않습니다.
- 문화상품권 후원에 따라서 보상을 서버 관리자 마음대로 지정할 수 있습니다. 

# Installation
1. 해당 플러그인을 사용하기 위해서는 먼저 라이센스를 구매하셔야 합니다. 
2. 라이센스는 https://cland.craftben.ch/ 에 들어가셔서 라이센스를 구매하시면 됩니다. 
3. 라이센스는 총 `aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa` 의 형식으로 구성이 되어있으며, 이 라이센스 키를 분실하실 시 재발급은 불가능합니다.
4. 발급받은 라이센스를 `config.yml` 에 작성하시면 됩니다.
5. 이후 [CulturelandPython](https://github.com/gooday2die/CulturelandPython) 을 통해 자체적으로 API 서버를 개방하고, 서버 플러그인과 연결을 하시면 됩니다.


# Commands
다음의 명령어들을 통해  서버에서 후원을 할 수 있습니다.

    /후원 1234-1234-1234-1234
    /문상 1234-1234-1234-1234
    /donate 1234-1234-1234-1234
    /redeem 1234-1234-1234-1234
# Permissions
다음의 Permission Node 를 통해 이 플러그인을 이용할 수 있습니다.
   - `ClandPlugin.redeem` : 위의 후원 명령어를 사용할 권한입니다.
   - `ClandPlugin.admin` : 후원 시 알림을 받을 관리자에 대한 권한입니다.

