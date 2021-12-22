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
  - repository/SearchRepository : view에 표시될 model 클래스로 매핑하고 Resource 클래스로 감싸 flow를 생성합니다.

Remote : API 호출로 Github Open API의 데이터를 가져오기 위한 클래스들을 모아놓은 패키지
  - dto : API 응답 JSON을 데이터클래스로 직렬화하기 위한 응답 클래스 (Gson 사용)
  - response/ApiResponse : API 응답을 상태와 데이터 함께 갖도록 하는 래퍼 클래스 (SUCCESS, ERROR)
  - GithubService : API 호출을 위한 인터페이스
  - NetworkModule : OkHttpClient, Gson, Retrofit 객체들을 갖고 있는 Object
  - RemoteDataSource : API 응답을 ApiResponse로 감싸서 flow를 생성합니다. 응답이 성공인경우 SUCCESS, 에러가 발생한 경우 ERROR

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
