# RetrofitErrorHandlingSample-Android

Retrofit2를 사용하여 RestAPI를 호출하고 view에 데이터를 표시하는 데모 앱 입니다.

## Structure

Github Open API에서 리포지토리를 검색하고 결과를 리사이클러뷰로 표시하는 간단한 구조입니다.

## Architecture

View
  - MainActivity : 리사이클러뷰가 있는 view 입니다. MainViewModel에 의존성을 가집니다.
  - MainAdapter : 리사이클러뷰를 위한 adapter 입니다. 리포지토리 검색 결과는 owner 아바타와 리포지토리 이름으로 데이터가 구성됩니다.

ViewModel
  - MainViewModel : view에 표시될 데이터와 상태를 갖고 있는 ViewModel 입니다.

Data : view에 표시되는 데이터를 갖고 있는 model과 데이터를 제공해주는 SearchRepository가 있습니다.
  - common/Resource : UI 상태를 데이터와 함께 갖고 있는 클래스 입니다.
  - common/Status : UI 상태를 나타내는 enum 클래스 입니다. (SUCCESS, ERROR, LOADING)
  - model/GithubRepo : view에 표시되는 리포지토리 결과 데이터들을 갖고 있는 model 입니다.
  - repository/SearchRepository : view에 표시될 model 클래스로 매핑하고 Resource 클래스로 감싸 Flow를 생성합니다.

Remote : API 호출로 Github Open API의 데이터를 가져오기 위한 클래스들을 모아놓은 패키지
  - dto : API 응답 JSON을 데이터클래스로 직렬화하기 위한 응답 클래스 (Gson 사용)
  - response/ApiResponse : API 응답을 상태와 데이터 함께 갖도록 하는 래퍼 클래스 (SUCCESS, ERROR)
  - GithubService : API 호출을 위한 인터페이스
  - NetworkModule : OkHttpClient, Gson, Retrofit 객체들을 갖고 있는 Object
  - RemoteDataSource : API 응답을 ApiResponse로 감싸서 Flow를 생성합니다. 응답이 성공인경우 SUCCESS, 에러가 발생한 경우 ERROR

## Logic

1. 사용자가 검색어를 입력하고 검색버튼을 누릅니다.
2. MainViewModel의 searchRepositories() 메소드 호출
3. SearchRepository의 검색 결과 flow를 collect
4. 처음 flow는 Resource.status = LOADING을 emit하고 RemoteDataSource의 API 응답 flow를 collect
5. view에서는 해당 Resource를 LiveData로 Observe 하고 있는데, LOADING 이므로 프로그레스 인디케이터 표시 (데이터 바인딩 사용)
6. RemoteDataSource에서 collect한 응답이 성공이면 Resource.status = SUCCESS, 데이터는 view에서 표시되는 model로 매핑하여 emit
7. 프로그레스 인디케이터를 사라지게 하고 리사이클러뷰에 리포지토리 검색 결과 리스트표시 (데이터 바인딩 사용)
8. 만약, API 응답이 ERROR로 왔다면 Resource.status = ERROR로 emit 되기 때문에 에러 스낵바 표시 (observe 코드에서 처리)

## Review

- 네트워크 응답의 성공과 에러의 일관된 처리를 위해 ApiResponse라는 래퍼 클래스를 사용
- view에서 UI 상태 관리를 위해 model과 status를 가진 Resource라는 래퍼 클래스를 사용
- Flow를 처음 사용해보아서 어려운 점이 많음 -> Kotlin Coroutines와 Flow 공부 필요
- Flow가 LiveData를 대체 할 수 있다고 하는데, 이렇게 하려면 lifecycle에 대한 따로 처리가 필요해서 flow를 collect하여 livedata로 바꿔서 view에서 observe 하였음 (.asLiveData()도 있더라)
- API 응답도 Flow를 생성하고, Repository에서도 검색 결과를 model로 매핑해서 Flow를 생성하는데 이렇게 두 개의 Flow가 존재하는게 맞는 패턴일까?
- 두 코루틴의 Dispatcher가 다르니까 에러가 뜨더라 -> 다른 Dispatcher를 쓰지 못하게 막은거 같다고 봄. flowOn() 메소드로 명시하라고 에러메시지에서 봄
- Coroutines, Flow, 비동기 개념에 대해 이해가 필요할 것 같다.
- 사용자가 리포지토리 검색 -> 로딩 표시(그 동안 API 호출, 응답 처리) -> view에 결과 보여줌(에러 발생 시 스낵바 표시)로 생각했던 로직이 그래도 잘 동작해서 만족스러움
- 사용자 이벤트가 의존성을 가진 방향으로 가고 데이터는 그 반대로 올라옴 (공식문서의 UDF 패턴과 동일하다고 생각함)
- 아키텍처와 코드에는 정답이 없으니까 공부하고 가다듬기. -> 아키텍처 적용한 샘플 TODO앱 만들어보기

## Update

- 네트워크 응답의 상태와 UI의 상태는 엄연히 다르다고 생각되어 그대로 두기로함.
- 네트워크 응답을 데이터 흐름이라고 볼 수 있는 Flow로 관리할 필요가 없다. -> API 호출 함수가 상태를 포함한 응답을 return 해주도록 하면 된다. Flow를 이중으로 쓸 필요가 없었음.
- 실제로 레트로핏은 코루틴과 함께 suspend 키워드를 붙여 사용하면 Response\<T\> 또는 dto 타입으로 반환값을 받을 수 있다. -> 레트로피 내부적으로 처리 (공부할 것)
- 네트워크 응답을 general하게 핸들링 하는 방법을 찾아보니 2가지 방법이 존재
  1. custom call adapter를 작성하여 핸들링하는 방식 [링크](https://proandroiddev.com/create-retrofit-calladapter-for-coroutines-to-handle-response-as-states-c102440de37a)
  2. Response\<T\> 타입의 응답을 처리해주는 공용함수를 사용 [링크](https://landroid.tistory.com/2)
- 두가지 방법모두 ApiResponse라는 래퍼 클래스로 네트워크 응답 상태를 관리, 방법의 차이
- 1번 방법은 example-01 브랜치에, 2번 방법은 example-02 브랜치에 따라 구현 해봤음
- 코드 분석, 공부해서 아키텍처 공부와 함께 응용해서 적용할 수 있을 것 같다. (ex. http 응답 코드별 사용자 에러 알림 구현이라든지, local data source와 함께 사용하는 방법이라든지..)
  
## Reference

[https://developer.android.com/kotlin/flow](https://developer.android.com/kotlin/flow)

[https://levelup.gitconnected.com/android-basic-app-using-mvvm-hilt-coroutines-flow-retrofit-and-coil-433763542ee0](https://levelup.gitconnected.com/android-basic-app-using-mvvm-hilt-coroutines-flow-retrofit-and-coil-433763542ee0)

[https://levelup.gitconnected.com/coroutines-retrofit-and-a-nice-way-to-handle-responses-769e013ee6ef](https://levelup.gitconnected.com/coroutines-retrofit-and-a-nice-way-to-handle-responses-769e013ee6ef)
