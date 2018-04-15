<html>
<head>
<title>SRDMS</title>
<script src = "https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js">
      </script>
      <script src = "https://code.highcharts.com/highcharts.js"></script> 
<style>
    tr{
      padding: 15px;
    }
    body{
    	margin:0px;
    	padding: 10px;
    }
    table,th,td{
    	border: 1px solid black;
    	padding: 5px;
    	text-align: center;
    }
  </style>
</head>
<body style="background-color: #7cb5e6">
    <h1 style="text-align:center;">Security Reference Database Management System</h1>
    <button onclick="getLatestApi()" id="getLatest">Get latest</button>
  <div id="json" style="float:left;width:50%"></div>
  <div hidden = "true" id="table" style="float:left;width:33%"></div>
  <div id="graph" style="float:left;width:50%">
  	<div id = "container"></div>
      <script type="text/javascript">
         $(document).ready();
      </script>
  </div>
  
  <script type="text/javascript">
    function getLatestApi()
    {
    	document.getElementById("getLatest").style.display="none";
     fetch("http://localhost:8080/SRDMS/webapi/stocks/latest").then(function(res){
        return res.json();
      }).then(function(data) {
        getLatest(data);
      });
    }
    function getLatest(data)
    {
      console.log(data);
      var x="<table height=100px border=2px><tr><th>Checked</th><th>ISIN</th><th>Stock Name</th><th>Price</th><th>Timestamp</th></tr>";
      for(i in data)
      {
    	  x+="<tr onclick=\"getHistory('"+data[i].isin+"')\"><td><input type=\"checkbox\"></td>";
        x+="<td>"+data[i].isin+"</td>";
        x+="<td>"+data[i].stockName+"</td>";
        x+="<td>"+data[i].price+"</td>";
        x+="<td>"+data[i].timestamp+"</td></tr>";
      }
      x+="</table>";
      document.getElementById("json").innerHTML=x;
    }
    function getHistory(isin)
    {
    	fetch("http://localhost:8080/SRDMS/webapi/stocks/"+isin).then(function(res){
    		return res.json();
    	}).then(function(data){
    		highcharts(data);
    	});
    }
    
    function highcharts(data) {
        var title = {
           text: 'Historic data'   
        };
        var subtitle = {
           text: ''
        };
        var xAxis = {
           categories: []
        };
        var yAxis = {
           title: {
              text: 'Price'
           },
           plotLines: [{
              value: 0,
              width: 1,
              color: '#808080'
           }]
        };   
        var tooltip = {
           valuePrefix: '₹'
        };
        var legend = {
           layout: 'vertical',
           align: 'right',
           verticalAlign: 'middle',
           borderWidth: 0
        };
        /*var series =  [{
              name: 'Tokyo',
              data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2,
                 26.5, 23.3, 18.3, 13.9, 9.6]
           }, 
           {
              name: 'New York',
              data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8,
                 24.1, 20.1, 14.1, 8.6, 2.5]
           }, 
           {
              name: 'London',
              data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 
                 16.6, 14.2, 10.3, 6.6, 4.8]
           }
        ];*/
        
        console.log(data)
        var series = [{
        	name: data[0].stockName,
        	data: []
        }];
        
        console.log(series);
        for (i in data) {
        	series[0].data.push(data[i].price);
        	xAxis.categories.push(data[i].timestamp);
        }
        	
        console.log(series)

        var json = {};
        json.title = title;
        json.subtitle = subtitle;
        json.xAxis = xAxis;
        json.yAxis = yAxis;
        json.tooltip = tooltip;
        json.legend = legend;
        json.series = series;
        
        $('#container').highcharts(json);
     }
    </script>
    
</body>
</html>