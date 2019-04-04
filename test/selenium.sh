#set -euo pipefail
#IFS=$'\n\t'

mkdir -p bin

cp=".:./lib/*"
bin="./bin"

#sudo systemctl start nginx && 
    # when launching nginx container give it --name "selenium_site"
    ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' selenium_site)
    javac -cp "$cp:$bin" -d "$bin" src/* &&
    java -DcontainerURL="http://${ip}" -cp "$cp:$bin" org.testng.TestNG testng.xml;
#sudo systemctl stop nginx
