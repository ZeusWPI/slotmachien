from flask import render_template, flash, redirect, request, url_for
from flask.ext.login import LoginManager, login_user
from flask.ext.admin import helpers


from app import app
from models import User, Token
from forms import LoginForm

login_manager = LoginManager()
login_manager.init_app(app)


@login_manager.user_loader
def load_user(userid):
    return User.query.filter_by(id = userid).first()

@app.route("/login", methods=["GET", "POST"])
def user_login():
    form = LoginForm()
    if helpers.validate_form_on_submit(form):
        user = form.get_user()
        login_user(user, remember = form.remember_me.data)
        flash("Logged in successfully.")
        return redirect(request.args.get("next") or url_for("admin.index"))
    return render_template("login/login.html", form=form)
