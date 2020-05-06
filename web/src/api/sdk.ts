import sdk from '../sdk.js';

export const SDK = new sdk.gitfox.JsSDK(
  new sdk.gitfox.entity.app.session.OAuthParams(
    'https://gitlab.com/',
    'd92ab730208fb57a32ae903d4b86cbfca87d839f7979b1768da1f1d60dab1343',
    '7222d2c9ccea96e16297d8b41fce87ebe2499ee3911dedd3844fe00ff5395016',
    'http://localhost:3000',
  ),
  true,
);
