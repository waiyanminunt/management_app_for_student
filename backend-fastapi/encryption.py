import os
from cryptography.fernet import Fernet

# The key must be a 32-url-safe-base64-encoded bytes string.
# In production, this MUST come from a secure environment variable.
# For demo purposes, we provide a fallback if none is provided.
ENCRYPTION_KEY = os.getenv("ENCRYPTION_KEY", Fernet.generate_key().decode())

cipher_suite = Fernet(ENCRYPTION_KEY.encode())

def encrypt_data(data: str) -> str:
    """Encrypts string data using AES-256 (Fernet)."""
    if data is None:
        return None
    return cipher_suite.encrypt(data.encode('utf-8')).decode('utf-8')

def decrypt_data(encrypted_data: str) -> str:
    """Decrypts string data using AES-256 (Fernet)."""
    if encrypted_data is None:
        return None
    return cipher_suite.decrypt(encrypted_data.encode('utf-8')).decode('utf-8')
