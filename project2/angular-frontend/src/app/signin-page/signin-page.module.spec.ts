import { SigninPageModule } from './signin-page.module';

describe('LoginPageModule', () => {
  let loginPageModule: SigninPageModule;

  beforeEach(() => {
    loginPageModule = new SigninPageModule();
  });

  it('should create an instance', () => {
    expect(loginPageModule).toBeTruthy();
  });
});
