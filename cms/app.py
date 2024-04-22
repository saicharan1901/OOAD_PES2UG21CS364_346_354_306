from flask import Flask, request, render_template, redirect, url_for
import subprocess
import json

app = Flask(__name__)

CLASSPATH = "/Users/cherry/Downloads/mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar:."


@app.route('/')
def index():
    return render_template('index.html')

@app.route('/user.html')
def user():
    return render_template('user.html')

@app.route('/police_login.html')
def police():
    return render_template('police_login.html')

@app.route('/police-dashboard/criminal_registration.html')
def criminalRegister():
    return render_template('criminal_registration.html')


@app.route('/register', methods=['POST'])
def register():
    username = request.form['username']
    password = request.form['password']

    print(f"Received data: Name - {username}, Password - {password}")

    # Call the Java program using subprocess
    subprocess.run(['java', '-cp', '/Users/cherry/Downloads/mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar:.', 'MySQLConnector', username, password])

    return render_template('dashboard.html')


@app.route('/login', methods=['POST'])
def login():
    admin_name = request.form['admin_name']
    password = request.form['password']

    print(f"Received login attempt: Admin Name - {admin_name}, Password - {password}")

    # Call the Java program using subprocess
    result = subprocess.run(['java', '-cp', '/Users/cherry/Downloads/mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar:.', 'PoliceLogin', admin_name, password], capture_output=True, text=True)

    print(result.stdout)
    
    if result.returncode == 0:
        if "Login successful!" in result.stdout:
            # Extracting police station ID from result
            station_id = result.stdout.split(":")[1].strip()
            return redirect(url_for('police_dashboard', station_id=station_id))
    
    return 'Invalid credentials. Please try again.'

@app.route('/police-dashboard/<int:station_id>')
def police_dashboard(station_id):
    # Call the Java program to get police station details
    result = subprocess.run(['java', '-cp', '/Users/cherry/Downloads/mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar:.', 'GetPoliceStationDetails', str(station_id)], capture_output=True, text=True)
    result_crime = subprocess.run(['java', '-cp', '/Users/cherry/Downloads/mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar:.', 'GetCrimeInfo', str(station_id)], capture_output=True, text=True)

    # Extract police station details from result
    station_details = result.stdout.split("\n")

    # Extract crime details from result_crime and manually parse it into a list of lists
    crime_details = []
    crime_data = result_crime.stdout.strip()[1:-1]  # Remove square brackets from the beginning and end
    final = crime_data.strip()[1:-1]
    print(final)
    for record in final.split("], ["):
        crime_details.append(record.split(", "))

    return render_template('police_dashboard.html', station_details=station_details, crime_details=crime_details)



@app.route('/register-crime', methods=['POST'])
def registerCrime():
    police_station_id = request.form['police_station_id']
    crime_type = request.form['crime_type']
    crime_description = request.form['crime_description']
    criminal_description = request.form['criminal_description']

    print(f"{police_station_id} {crime_type} {crime_description} {criminal_description}")

    # Call the Java program using subprocess
    subprocess.run(['java', '-cp', '/Users/cherry/Downloads/mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar:.', 'CrimeRegister', police_station_id, crime_type, crime_description, criminal_description])

    return 'Crime registered successfully!'



@app.route('/register-criminal/<int:station_id>', methods=['GET', 'POST'])
def register_criminal(station_id):
    if request.method == 'POST':
        name = request.form['name']
        crime_id = request.form['crime_id']

        # Call the Java program using subprocess with the classpath
        result = subprocess.run(['java', '-cp', CLASSPATH, 'RegisterCriminal', name, crime_id], capture_output=True, text=True)

        print(result)

        if result.returncode == 0:
            return 'Criminal registered successfully!'
        else:
            return 'Error registering criminal.'
    else:
        # Call the Java program to get crime IDs for the specified police station
        result = subprocess.run(['java', '-cp', CLASSPATH, 'GetCrimeIDs', str(station_id)], capture_output=True, text=True)
        crime_ids = result.stdout.split('\n')

        return render_template('criminal_registration.html', station_id=station_id, crime_ids=crime_ids)


if __name__ == '__main__':
    app.run(debug=True)
