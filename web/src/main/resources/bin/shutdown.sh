#TODO shutdown.sh
file="custom.pid"
if [ -f "$file" ]
then
        echo "Stopping micro service, this will take some seconds, please wait."
        processId=$(cat custom.pid)
        kill -9 $(cat custom.pid)
        rm -f "$file"
else
        echo "The custom.pid file does not exist or may was deleted, please stop the micro service 'erp2' manually"
fi
