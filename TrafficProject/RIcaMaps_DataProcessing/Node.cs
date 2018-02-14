using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;

namespace RIcaMaps_DataProcessing
{
    class Node
    {
        public List<Node> neighbours;
        public GeoCoordinate location;
        public List<Line> containingLines;
        public Node(GeoCoordinate loc)
        {
            location = loc;
            neighbours = new List<Node>();
            containingLines = new List<Line>();
        }
        public double GetDistanceToNode(Node n)
        {
            return location.GetDistanceTo(n.location);
        }
        public string toString()
        {
            return "["+location.Latitude.ToString()+","+location.Longitude.ToString()+"]";
        }
    }
}
