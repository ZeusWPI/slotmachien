from flask.ext.wtf import Form
from wtforms import StringField, BooleanField
from wtforms.validators import DataRequired, ValidationError


from models import User, Token
class LoginForm(Form):
    username = StringField('username', validators=[DataRequired()])
    password = StringField('password', validators=[DataRequired()])
    remember_me = BooleanField('remember_me', default=False)

    def validate_username(self, field):
        user = self.get_user()

        if user is None:
            raise ValidationError('Invalid user')

        if self.get_token() is None:
            raise ValidationError('Invalid password')

    def get_user(self):
        return User.query.filter_by(username=self.username.data).first()

    def get_token(self):
        return Token.query.filter_by(token=self.password.data, user=self.get_user()).first()
