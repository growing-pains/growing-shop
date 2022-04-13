# growing-shop

* 내 자신이 성장할 수 있도록 해보고 싶고 배워보고 싶고 해볼 것들을 마음껏 활용하는 쇼핑몰
* 최소한의 기능을 작성하고 확장하는 형태로 진행한다

## 요구사항

1. [유저](./설계/유저)
   * 유저는 하나의 유형에 속해있어야 한다
     * 일반 유저는 하나의 등급에 속해있다
     * 판매자는 하나의 업체에 반드시 속해있어야 하며, 업체 등급이 존재한다
     * 관리자는 모든 기능을 사용할 수 있다
   * 패스워드는 암호화 되어 통신 및 저장되어야 한다
2. [상품](./설계/상품)
   * 상품에는 이름과 가격이 존재한다
   * 판매자 계정은 상품 등록/삭제/수정이 가능하다
3. [주문](./설계/주문)
    * 하나의 주문에 여러 상품, 개수가 지정되어 생성될 수 있다
4. 결제
   * 외부 결제 시스템을 사용한다
   * 해당 프로젝트에서는 호출 시 0.01% 확률로 에러가 발생하는 Mock 결제 시스템을 연동할 예정
    
## Aggregate

![img.png](설계/img/Overall%20Aggregate.png)

### 구현 방식

* 기본적으로 TDD 방식으로 작성한다
* 사용해보고 싶은 모든 프레임워크, 언어를 모두 구현한다
    * Spring(Java)
    * Spring(Kotlin)
    * Go
    * node.js (nestjs)
