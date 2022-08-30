# Torch Donation
![language](https://img.shields.io/github/languages/top/MinecraftTorch/TorchDonation) [![CodeFactor](https://www.codefactor.io/repository/github/minecrafttorch/TorchDonation/badge)](https://www.codefactor.io/repository/github/minecrafttorch/navercafealert) ![downloads](https://img.shields.io/github/downloads/MinecraftTorch/TorchDonation/total) ![enter image description here](https://img.shields.io/github/license/MinecraftTorch/TorchDonation) ![enter image description here](https://img.shields.io/github/v/release/MinecraftTorch/TorchDonation)
## 경고
- 해당 플러그인을 사용하며 생기는 모든 문제 및 책임은 사용자 본인에게 있습니다.
- 이 플러그인은 이런 방식으로 이론상 동작할 수 있다는 것을 알려주기 위한 플러그인입니다.
- 컬쳐랜드 직원분들이 플러그인을 내려달라고 요청하신다면 언제든 없어질 수 있는 플러그인입니다.

## 소개
### 문화상품권 후원 자동처리 플러그인
-   문화상품권을 자동으로 충전하고 이에 따른 보상을 지급하는 플러그인입니다.
-   모든 소스는 오픈소스로 공개했습니다.
-   컬쳐랜드 직원분들의 심기가 불편하시면 없어질 수 있는 플러그인입니다.
-   개인 서버에서 사용할 예정으로 제작한 플러그인이였으나, 서버 제작이 파기되는 바람에 그냥 올립니다.

## 기능
- Asynchronous 하게 동작하기에 서버에 랙을 유발하지 않습니다.
- 기존 버전(ID, 비밀번호) 로그인 방식보다 많이 빠릅니다.
- MySQL 서버에 후원 정보를 저장하거나, 아니면 SQLite 파일에 후원 기록을 저장할 수 있습니다.
-   간단한 명령어를 통해서 문화상품권 후원을 처리할 수 있습니다.
- 만일 후원이 여러개가 한번에 처리해야할 시, Queue 방식으로 처리하여 순차적으로 처리합니다 (Race condition 방지)

## 사용법
### Cookie 설정 
사용법이 조금 복잡하니, https://github.com/MinecraftTorch/TorchDonation/wiki/Cookie-%EC%83%9D%EC%84%B1-%EB%B0%A9%EB%B2%95 에서 내용을 확인해주세요.
### Config.yml 설정
https://github.com/MinecraftTorch/TorchDonation/wiki/Config.yml 를 확인해주세요.

## 데모 영상
원래 플러그인을 작동시는 Chrome 이 뜨지 않습니다. 단지 테스트 용도로 여러분들께 페이지를 보여드리기 위해서 나오는 이미지입니다.

https://user-images.githubusercontent.com/49092508/185739955-16865220-f52a-4828-8c02-3f82acf5f2ad.mp4

https://user-images.githubusercontent.com/49092508/185739957-c1da0411-6f94-4b57-9de8-a6d713fe6f62.mp4

