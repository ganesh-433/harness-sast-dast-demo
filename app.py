import sqlite3
from flask import Flask, request

app = Flask(__name__)

# Set the path to the database file
DATABASE_FILE = 'database.db'

# Vulnerable to SQL injection!
@app.route('/users')
def get_user():
    user_id = request.args.get('id')
    
    conn = sqlite3.connect(DATABASE_FILE)
    cursor = conn.cursor()
    
    query = "SELECT * FROM users WHERE id = " + user_id
    
    try:
        cursor.execute(query)
        user = cursor.fetchone()
        return f"User details: {user}"
    except Exception as e:
        return f"An error occurred: {e}"
        
@app.route('/')
def home():
    return "Welcome to the home page! Try /users?id=1"

if __name__ == '__main__':
    # Initialize the database on startup
    with sqlite3.connect(DATABASE_FILE) as conn:
        cursor = conn.cursor()
        cursor.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT)")
        conn.commit()

    app.run(host='0.0.0.0', port=5000)
