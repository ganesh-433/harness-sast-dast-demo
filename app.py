import sqlite3
from flask import Flask, request

app = Flask(__name__)

# This is a simple, insecure endpoint
@app.route('/users')
def get_user():
    # Vulnerable to SQL injection!
    user_id = request.args.get('id')
    
    conn = sqlite3.connect('database.db')
    cursor = conn.cursor()
    
    query = "SELECT * FROM users WHERE id = " + user_id
    
    try:
        cursor.execute(query)
        user = cursor.fetchone()
        return f"User details: {user}"
    except Exception as e:
        return f"An error occurred: {e}"
        
# For DAST scan to hit a public endpoint
@app.route('/')
def home():
    return "Welcome to the home page! Try /users?id=1"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
