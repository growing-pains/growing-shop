## 업체

* `이름/사업자등록번호/상태/등급` 값을 가질 수 있다
  * 이름: 필수이며, 한글/영어/특정 특수문자('-.()[]) 만 포함된 30 자 이하이어야 한다
  * 사업자등록번호: 필수이며 [링크](http://seoulcredit.co.kr/business_id) 와 같은 validation 을 통과해야 한다
  * 상태: `NORMAL`/`UNDER_REVIEW`/`DELETED` 중 하나에 속해야 한다
  * 등급: `Bronze`/`Silver`/`Gold`/`Platinum`/`Diamond` 중 하나에 속해야 한다
* 업체를 새로 생성 시 상태는 `UNDER_REVIEW` 으로 생성된다
