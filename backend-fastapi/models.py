import enum
from sqlalchemy import Column, Integer, String, Enum, ForeignKey, DateTime
from sqlalchemy.sql import func
from sqlalchemy.orm import relationship
from database import Base

class UserRole(str, enum.Enum):
    ADMIN = "ADMIN"
    TEACHER = "TEACHER"
    FINANCE = "FINANCE"
    STUDENT = "STUDENT"

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    email = Column(String, unique=True, index=True, nullable=False)
    password_hash = Column(String, nullable=False)
    role = Column(Enum(UserRole), nullable=False, default=UserRole.STUDENT)
    
    financial_records = relationship("FinancialRecord", back_populates="student")

class FinancialRecord(Base):
    """
    Stores sensitive financial installment data.
    The 'amount_encrypted' and 'notes_encrypted' fields will be encrypted via AES-256.
    """
    __tablename__ = "financial_records"
    
    id = Column(Integer, primary_key=True, index=True)
    student_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    amount_encrypted = Column(String, nullable=False)
    notes_encrypted = Column(String, nullable=True)
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    
    student = relationship("User", back_populates="financial_records")

class SystemLog(Base):
    """
    Audit logging table for administrative actions.
    """
    __tablename__ = "system_logs"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, nullable=True) # ID of the admin/teacher making the change
    action = Column(String, nullable=False) # e.g., "Updated Student Record"
    endpoint = Column(String, nullable=False)
    ip_address = Column(String, nullable=True)
    user_agent = Column(String, nullable=True)
    timestamp = Column(DateTime(timezone=True), server_default=func.now())



class FeedbackSentiment(str, enum.Enum):
    POSITIVE = "POSITIVE"
    NEUTRAL = "NEUTRAL"
    NEGATIVE = "NEGATIVE"


class Feedback(Base):
    """
    Stores anonymized student feedback for sentiment analysis and predictive analytics.
    Links to users (students) and captures batch information.
    """
    __tablename__ = "feedbacks"

    id = Column(Integer, primary_key=True, index=True)
    student_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    batch = Column(String, nullable=False)            # e.g., "Batch A", "Batch B"
    content = Column(String, nullable=False)          # anonymized feedback text
    sentiment = Column(Enum(FeedbackSentiment), nullable=False, default=FeedbackSentiment.NEUTRAL)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

    student = relationship("User", backref="feedbacks")

