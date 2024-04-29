Easy login with Google, Facebook, VK for android apps

## Use:

```kotlin
    fun loginWithGoogle() {
        startActivityForResult(SocialLogin.loginIntent(this, LoginType.GOOGLE), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val loginResult: LoginResult? = data?.loginResult
        // do something
    }
    // OR
    fun loginWithGoogle() {
        googleLoginResultListener.launch(
            SocialLogin.loginIntent(
                this, SocialLogin.LoginType.GOOGLE
            )
        )
    }

    private val googleLoginResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val loginResult = result.data?.loginResult
        // do something 
    }
```
#### Get user info:

```kotlin
    val withInfo = SocialLogin.getResultWithUserInfo(loginResult)
    val avatar = withInfo.avatar
    val email = withInfo.email
    val firstName = withInfo.firstName
    val lastName = withInfo.lastName
```
#### Request other permissions:
```kotlin
    // for Facebook
    SocialLogin.loginIntent(this, LoginType.FB, "user_friends")
```

#### Then add the received resources to your app:
```xml
<resources>

  <!--For Google (optional)-->
  <string name="default_web_client_id">...</string>

  <!--For Facebook (required)-->
  <string name="facebook_app_id">...</string>
  <string name="fb_login_protocol_scheme">...</string>
    
</resources>
```