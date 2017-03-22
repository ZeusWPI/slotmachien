"""Initial revision

Revision ID: 4f286e62e8e7
Revises: None
Create Date: 2015-06-04 16:05:57.380828

"""

# revision identifiers, used by Alembic.
from app import db
from models import User
from sqlalchemy.sql import table, column

revision = '4f286e62e8e7'
down_revision = None

from alembic import op
import sqlalchemy as sa


def upgrade():
    ### commands auto generated by Alembic - please adjust! ###
    op.create_table('service_token',
    sa.Column('service', sa.String(length=20), nullable=False),
    sa.Column('key', sa.String(length=120), nullable=True),
    sa.PrimaryKeyConstraint('service')
    )
    op.create_table('user',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('username', sa.String(length=80), nullable=True),
    sa.Column('allowed', sa.Boolean(), nullable=True),
    sa.Column('admin', sa.Boolean(), nullable=True),
    sa.PrimaryKeyConstraint('id'),
    sa.UniqueConstraint('username')
    )
    op.create_table('token',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('token', sa.String(length=120), nullable=True),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.Column('created_on', sa.DateTime(), nullable=True),
    sa.Column('expires_on', sa.DateTime(), nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['user.id'], ),
    sa.PrimaryKeyConstraint('id'),
    sa.UniqueConstraint('token')
    )
    op.create_table('log_action',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.Column('action', sa.String(length=15), nullable=True),
    sa.Column('logged_on', sa.DateTime(), nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['user.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    ### end Alembic commands ###

    # Data upgrades
    user_table = table('user',
                       column('id', sa.Integer()),
                       column('username', sa.String(length=80)),
                       column('allowed', sa.Boolean()),
                       column('admin', sa.Boolean()),
                       )

    op.bulk_insert(user_table,
                   [
                       {'id': 1, 'username': 'feliciaan', 'allowed': True, 'admin': True},
                       {'id': 2, 'username': 'don', 'allowed': True, 'admin': True},
                   ]
                   )

def downgrade():
    ### commands auto generated by Alembic - please adjust! ###
    op.drop_table('log_action')
    op.drop_table('token')
    op.drop_table('user')
    op.drop_table('service_token')
    ### end Alembic commands ###


