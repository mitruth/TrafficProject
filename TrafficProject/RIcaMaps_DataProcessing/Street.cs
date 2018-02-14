using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;

namespace RIcaMaps_DataProcessing
{
    class Street
    {
        public String name;
        public List<Line> polyLine;
        public List<int> delays;

        public Street(String name)
        {
            this.name = name;
            polyLine = new List<Line>();
            delays = new List<int>();
        }

        public void addLine(Line l)
        {
            polyLine.Add(l);
        }
        public double getLength()
        {
            double length=0;
            foreach(Line l in polyLine)
                length += l.getLength();
            return length;
        }
        public String toString()
        {
            String street = name+"\n";
            foreach (Line l in polyLine)
                street += l.toString() + "\n";
            for (int i= 0;i < 24;i++)
                street += delays[i]+ " ";
            return street.Remove(street.Length - 1);
        }
        public void setDelaysOnLines()
        {
            foreach (Line l in polyLine)
                l.setDelays(delays);
        }
    }
}
