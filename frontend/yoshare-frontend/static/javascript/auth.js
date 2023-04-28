export var Auth = {
  vars: {
    lowin: Element,
    lowin_brand: Element,
    lowin_wrapper: Element,
    lowin_login: Element,
    lowin_wrapper_height: 0,
    login_back_link: Element,
    forgot_link: Element,
    login_link: Element,
    login_btn: Element,
    register_link: Element,
    password_group: Element,
    password_group_height: 0,
    lowin_register: Element,
    lowin_footer: Element,
    box: null,
    option: {},
    verify_btn: Element,
    verify_input: Element,
    countdown_flag: 60
  },
  register(e) {
    Auth.vars.lowin_login.className += " lowin-animated";
    setTimeout(() => {
      Auth.vars.lowin_login.style.display = "none";
    }, 500);
    Auth.vars.lowin_register.style.display = "block";
    Auth.vars.lowin_register.className += " lowin-animated-flip";

    Auth.setHeight(
      Auth.vars.lowin_register.offsetHeight +
        Auth.vars.lowin_footer.offsetHeight
    );

    e.preventDefault();
  },
  login(e) {
    Auth.vars.lowin_register.classList.remove("lowin-animated-flip");
    Auth.vars.lowin_register.className += " lowin-animated-flipback";
    Auth.vars.lowin_login.style.display = "block";
    Auth.vars.lowin_login.classList.remove("lowin-animated");
    Auth.vars.lowin_login.className += " lowin-animatedback";
    setTimeout(() => {
      Auth.vars.lowin_register.style.display = "none";
    }, 500);

    setTimeout(() => {
      Auth.vars.lowin_register.classList.remove("lowin-animated-flipback");
      Auth.vars.lowin_login.classList.remove("lowin-animatedback");
    }, 1000);

    Auth.setHeight(
      Auth.vars.lowin_login.offsetHeight + Auth.vars.lowin_footer.offsetHeight
    );

    e.preventDefault();
  },
  forgot(e) {
    Auth.vars.password_group.classList += " lowin-animated";
    Auth.vars.login_back_link.style.display = "block";

    setTimeout(() => {
      Auth.vars.login_back_link.style.opacity = 1;
      Auth.vars.password_group.style.height = 0;
      Auth.vars.password_group.style.margin = 0;
    }, 100);

    Auth.vars.login_btn.innerText = "找回密码";

    Auth.setHeight(
      Auth.vars.lowin_wrapper_height - Auth.vars.password_group_height
    );
    Auth.vars.lowin_login
      .querySelector("form")
      .setAttribute("action", Auth.vars.option.forgot_url);

    e.preventDefault();
  },
  loginback(e) {
    Auth.vars.password_group.classList.remove("lowin-animated");
    Auth.vars.password_group.classList += " lowin-animated-back";
    Auth.vars.password_group.style.display = "block";

    setTimeout(() => {
      Auth.vars.login_back_link.style.opacity = 0;
      Auth.vars.password_group.style.height =
        Auth.vars.password_group_height + "px";
      Auth.vars.password_group.style.marginBottom = 30 + "px";
    }, 100);

    setTimeout(() => {
      Auth.vars.login_back_link.style.display = "none";
      Auth.vars.password_group.classList.remove("lowin-animated-back");
    }, 1000);

    Auth.vars.login_btn.innerText = "登 录";
    Auth.vars.lowin_login
      .querySelector("form")
      .setAttribute("action", Auth.vars.option.login_url);

    Auth.setHeight(Auth.vars.lowin_wrapper_height);

    e.preventDefault();
  },
  setHeight(height) {
    Auth.vars.lowin_wrapper.style.minHeight = height + "px";
  },
  brand() {
    Auth.vars.lowin_brand.classList += " lowin-animated";
    setTimeout(() => {
      Auth.vars.lowin_brand.classList.remove("lowin-animated");
    }, 1000);
  },

  init(option) {
    (this.vars.lowin = document.querySelector(".lowin")),
      (this.vars.lowin_brand = document.querySelector(".lowin-brand")),
      (this.vars.lowin_wrapper = document.querySelector(".lowin-wrapper")),
      (this.vars.lowin_login = document.querySelector(".lowin-login")),
      (this.vars.lowin_wrapper_height = 0),
      (this.vars.login_back_link = document.querySelector(".login-back-link")),
      (this.vars.forgot_link = document.querySelector(".forgot-link")),
      (this.vars.login_link = document.querySelector(".login-link")),
      (this.vars.login_btn = document.querySelector(".login-btn")),
      (this.vars.register_link = document.querySelector(".register-link")),
      (this.vars.password_group = document.querySelector(".password-group")),
      (this.vars.password_group_height = 0),
      (this.vars.lowin_register = document.querySelector(".lowin-register")),
      (this.vars.lowin_footer = document.querySelector(".lowin-footer")),
      (this.vars.box = document.getElementsByClassName("lowin-box")),
      (this.vars.verify_btn = document.querySelector(".verify-btn")),
      (this.vars.verify_input = document.querySelector(".verify-input"));
    Auth.setHeight(
      Auth.vars.box[0].offsetHeight + Auth.vars.lowin_footer.offsetHeight
    );
    Auth.vars.password_group.style.height =
      Auth.vars.password_group.offsetHeight + "px";
    Auth.vars.password_group_height = Auth.vars.password_group.offsetHeight;
    Auth.vars.lowin_wrapper_height = Auth.vars.lowin_wrapper.offsetHeight;

    Auth.vars.option = option;
    Auth.vars.lowin_login
      .querySelector("form")
      .setAttribute("action", option.login_url);

    let len = Auth.vars.box.length - 1;

    for (let i = 0; i <= len; i++) {
      if (i !== 0) {
        Auth.vars.box[i].className += " lowin-flip";
      }
    }

    Auth.vars.forgot_link.addEventListener("click", e => {
      Auth.forgot(e);
    });

    Auth.vars.register_link.addEventListener("click", e => {
      Auth.brand();
      Auth.register(e);
    });

    Auth.vars.login_link.addEventListener("click", e => {
      Auth.brand();
      Auth.login(e);
    });

    Auth.vars.login_back_link.addEventListener("click", e => {
      Auth.loginback(e);
    });
  }
};
