from pydantic import BaseModel, EmailStr
from typing import Optional
from models import UserRole

# -------------------------
# User Schemas
# -------------------------
class UserBase(BaseModel):
    name: str
    email: EmailStr
    role: UserRole = UserRole.STUDENT

class UserCreate(UserBase):
    password: str

class UserOut(UserBase):
    id: int
    
    class Config:
        from_attributes = True

# -------------------------
# Auth Schemas
# -------------------------
class Token(BaseModel):
    access_token: str
    token_type: str
    role: UserRole

class LoginRequest(BaseModel):
    email: str
    passwordHash: str  # For compatibility with frontend interface

# -------------------------
# Financial Schemas
# -------------------------
class FinancialRecordCreate(BaseModel):
    student_id: int
    amount: str  # Kept as string for simplicity of encryption demo
    notes: Optional[str] = None

class FinancialRecordOut(BaseModel):
    id: int
    student_id: int
    amount_decrypted: str
    notes_decrypted: Optional[str] = None

    class Config:
        from_attributes = True
