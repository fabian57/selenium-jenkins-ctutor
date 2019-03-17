pipeline {
    agent { label 'docker-selenium-slave' }
    stages {
        //stage ("Checkout") {
        //    steps {
        //        echo 'Pull site and tests'
        //        git url: 'https://github.com/fabian57/selenium-jenkins-demo.git'
        //    }
        //}
        stage ("Start server") {
            steps {
                echo 'Copy website into volume'
                sh 'echo "jenkins" | sudo -S cp -r www/* /data'
                
                echo 'Start nginx container'
                sh 'echo "jenkins" | sudo -S docker run -d -v jenkins_node:/usr/share/nginx/html -p 8080:80 --name selenium_site nginx'
            }
        }
        stage ("Testing") {
            steps {
                echo 'Start test'
                sh 'cd selenium_demo && echo "jenkins" | sudo -S ./selenium.sh'
            }
        }
        stage ("Clean up") {
            steps {
                echo 'Remove file'
                sh 'echo "jenkins" | sudo -S rm -rf /data/*'
            
                echo 'Stop nginx container'
                sh 'echo "jenkins" | sudo -S docker stop selenium_site'
                echo 'Delete nginx container'
                sh 'echo "jenkins" | sudo -S docker rm selenium_site'
            }
        }
    }
}
