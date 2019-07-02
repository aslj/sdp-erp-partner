#TODO status.sh

file="custom.pid"
if [ -f "$file" ]
then
        ps cax | grep $(cat custom.pid) > /dev/null
        if [ $? -eq 0 ];
        then
                echo "The micro service is currently running, you need to execute the shutdown script to stop the micro service"
        else
                echo "The micro service is not running, you need to execute the startup script to start the micro service"
        fi
else
        echo "The micro service is not running, you need to execute the startup script to start the micro service"
fi