import * as React from 'react';

import { SDK } from 'api/sdk';

export const useAuth = () => {
  React.useEffect(() => {
    const currentUrl = window.location.href;

    const isRedirected = performance.navigation.type === 0;
    const isValid = SDK.getSessionInteractor().checkOAuthRedirect(currentUrl);

    if (isValid && !isRedirected) {
      // const r = SDK.getSessionInteractor().login(currentUrl);
      // SDK.getProjectInteractor()
      //   .getProject(2977308)
      //   .then((project, err) => console.log(project));
    } else {
      const oAuthUrl = SDK.getSessionInteractor().oauthUrl;
      window.location.replace(oAuthUrl);
    }
  }, []);
};
